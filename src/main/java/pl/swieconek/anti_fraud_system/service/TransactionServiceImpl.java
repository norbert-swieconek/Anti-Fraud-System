package pl.swieconek.anti_fraud_system.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.swieconek.anti_fraud_system.model.*;
import pl.swieconek.anti_fraud_system.dto.TransactionRequest;
import pl.swieconek.anti_fraud_system.dto.TransactionResponse;
import pl.swieconek.anti_fraud_system.repository.StolenCardRepository;
import pl.swieconek.anti_fraud_system.repository.SuspiciousIpRepository;
import pl.swieconek.anti_fraud_system.repository.TransactionRepository;
import pl.swieconek.anti_fraud_system.validator.DataValidator;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final DataValidator dataValidator;
    private final SuspiciousIpRepository suspiciousIpRepository;
    private final StolenCardRepository stolenCardRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(DataValidator dataValidator, SuspiciousIpRepository suspiciousIpRepository, StolenCardRepository stolenCardRepository, TransactionRepository transactionRepository) {
        this.dataValidator = dataValidator;
        this.suspiciousIpRepository = suspiciousIpRepository;
        this.stolenCardRepository = stolenCardRepository;
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse processTx(TransactionRequest transactionRequest) {
        if (!dataValidator.validateCard(transactionRequest.number()) || !dataValidator.validateIp(transactionRequest.ip())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (!dataValidator.checkRegion(transactionRequest.region())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (transactionRequest.amount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        List<String> prohibitedReasons = new ArrayList<>();
        List<String> manualReasons = new ArrayList<>();

        Optional<SuspiciousIp> suspiciousIp = suspiciousIpRepository.findByIp(transactionRequest.ip());
        Optional<StolenCard> stolenCard = stolenCardRepository.findByNumber(transactionRequest.number());

        if (suspiciousIp.isPresent()) {
            prohibitedReasons.add("ip");
        }
        if (stolenCard.isPresent()) {
            prohibitedReasons.add("card-number");
        }
        if (transactionRequest.amount() > 1500) {
            prohibitedReasons.add("amount");
        }
        if (transactionRequest.amount() > 200 && transactionRequest.amount() <= 1500) {
            manualReasons.add("amount");
        }

        LocalDateTime endDate = transactionRequest.date();
        LocalDateTime startDate = endDate.minusHours(1);
        List<Transaction> history = transactionRepository.findByNumberAndDateGreaterThanAndDateLessThan(transactionRequest.number(), startDate, endDate);

        long uniqueIps = history.stream()
                .map(Transaction::getIp)
                .filter(ip -> !Objects.equals(ip, transactionRequest.ip()))
                .distinct()
                .count();

        long uniqueRegions = history.stream()
                .map(Transaction::getRegion)
                .filter(region -> !Objects.equals(region.toString(), transactionRequest.region()))
                .distinct()
                .count();

        if (uniqueIps > 2) {
            prohibitedReasons.add("ip-correlation");
        }
        if (uniqueRegions > 2) {
            prohibitedReasons.add("region-correlation");
        }
        if (uniqueIps == 2) {
            manualReasons.add("ip-correlation");
        }
        if (uniqueRegions == 2) {
            manualReasons.add("region-correlation");
        }

        Transaction transaction = new Transaction(transactionRequest.amount(), transactionRequest.ip(), transactionRequest.number(), Region.valueOf(transactionRequest.region()), transactionRequest.date());
        transactionRepository.save(transaction);

        if (!prohibitedReasons.isEmpty()){
            Collections.sort(prohibitedReasons);
            String info = String.join(", ", prohibitedReasons);
            return new TransactionResponse(Status.PROHIBITED.toString(), info);
        } else if (!manualReasons.isEmpty()) {
            Collections.sort(manualReasons);
            String info = String.join(", ", manualReasons);
            return new TransactionResponse(Status.MANUAL_PROCESSING.toString(), info);
        } else {
            return new TransactionResponse(Status.ALLOWED.toString(), "none");
        }
    }
}
