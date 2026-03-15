package pl.swieconek.anti_fraud_system.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.swieconek.anti_fraud_system.dto.*;
import pl.swieconek.anti_fraud_system.model.StolenCard;
import pl.swieconek.anti_fraud_system.model.SuspiciousIp;
import pl.swieconek.anti_fraud_system.repository.StolenCardRepository;
import pl.swieconek.anti_fraud_system.repository.SuspiciousIpRepository;
import pl.swieconek.anti_fraud_system.validator.DataValidator;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AntiFraudServiceImpl implements AntiFraudService {
    private final StolenCardRepository stolenCardRepository;
    private final SuspiciousIpRepository suspiciousIpRepository;
    private final DataValidator dataValidator;

    public AntiFraudServiceImpl(StolenCardRepository stolenCardRepository, SuspiciousIpRepository suspiciousIpRepository, DataValidator dataValidator) {
        this.stolenCardRepository = stolenCardRepository;
        this.suspiciousIpRepository = suspiciousIpRepository;
        this.dataValidator = dataValidator;
    }

    @Override
    public IpResponse addIpBlackList(IpRequest ipRequest) {
        if (!dataValidator.validateIp(ipRequest.ip())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<SuspiciousIp> existSuspiciousIp = suspiciousIpRepository.findByIp(ipRequest.ip());

        if (existSuspiciousIp.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        SuspiciousIp suspiciousIp = suspiciousIpRepository.save(new SuspiciousIp(ipRequest.ip()));

        return new IpResponse(suspiciousIp.getId(), suspiciousIp.getIp());
    }

    @Override
    public CardNumberResponse addCardNumberBlackList(CardNumberRequest cardNumberRequest) {
        if (!dataValidator.validateCard(cardNumberRequest.number())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<StolenCard> existStolenCard = stolenCardRepository.findByNumber(cardNumberRequest.number());

        if (existStolenCard.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        StolenCard stolenCard = stolenCardRepository.save(new StolenCard(cardNumberRequest.number()));

        return new CardNumberResponse(stolenCard.getId(), stolenCard.getNumber());
    }

    @Override
    public DeleteStatusResponse removeIpBlackList(String ip) {
        if (!dataValidator.validateIp(ip)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<SuspiciousIp> existSuspiciousIp = suspiciousIpRepository.findByIp(ip);

        if (existSuspiciousIp.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        suspiciousIpRepository.delete(existSuspiciousIp.get());

        return new DeleteStatusResponse("IP " + ip + " successfully removed!");
    }

    @Override
    public DeleteStatusResponse removeCardNumberBlackList(String cardNumber) {
        if (!dataValidator.validateCard(cardNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<StolenCard> existStolenCard = stolenCardRepository.findByNumber(cardNumber);

        if (existStolenCard.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        stolenCardRepository.delete(existStolenCard.get());

        return new DeleteStatusResponse("Card " + cardNumber + " successfully removed!");
    }

    @Override
    public List<IpResponse> getBlackListIps() {
        return suspiciousIpRepository.findAll().stream()
                .map(suspiciousIp -> new IpResponse(suspiciousIp.getId(), suspiciousIp.getIp()))
                .sorted(Comparator.comparing(IpResponse::id))
                .toList();
    }

    @Override
    public List<CardNumberResponse> getBlackListCardNumbers() {
        return stolenCardRepository.findAll().stream()
                .map(stolenCard -> new CardNumberResponse(stolenCard.getId(), stolenCard.getNumber()))
                .sorted(Comparator.comparing(CardNumberResponse::id))
                .toList();
    }
}
