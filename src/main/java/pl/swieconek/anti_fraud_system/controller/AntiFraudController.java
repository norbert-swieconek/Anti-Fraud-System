package pl.swieconek.anti_fraud_system.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.swieconek.anti_fraud_system.dto.*;
import pl.swieconek.anti_fraud_system.service.AntiFraudService;
import pl.swieconek.anti_fraud_system.service.TransactionService;
import pl.swieconek.anti_fraud_system.service.TransactionServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud")
public class AntiFraudController {
    private final TransactionService transactionService;
    private final AntiFraudService antiFraudService;

    public AntiFraudController(TransactionService transactionService, AntiFraudService antiFraudService) {
        this.transactionService = transactionService;
        this.antiFraudService = antiFraudService;
    }

    @PostMapping("/suspicious-ip")
    public ResponseEntity<IpResponse> addIpToBlackList(@RequestBody @Valid IpRequest ipRequest) {
        return ResponseEntity.ok(antiFraudService.addIpBlackList(ipRequest));
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<DeleteStatusResponse> removeIpFromBlackList(@PathVariable String ip) {
        return ResponseEntity.ok(antiFraudService.removeIpBlackList(ip));
    }

    @GetMapping("/suspicious-ip")
    public ResponseEntity<List<IpResponse>> getIpBlackList() {
        return ResponseEntity.ok(antiFraudService.getBlackListIps());
    }

    @PostMapping("/stolencard")
    public ResponseEntity<CardNumberResponse> addCardToBlackList(@RequestBody @Valid CardNumberRequest cardNumberRequest) {
        return ResponseEntity.ok(antiFraudService.addCardNumberBlackList(cardNumberRequest));
    }

    @DeleteMapping("/stolencard/{number}")
    public ResponseEntity<DeleteStatusResponse> removeCardFromBlackList(@PathVariable String number) {
        return ResponseEntity.ok(antiFraudService.removeCardNumberBlackList(number));
    }

    @GetMapping("/stolencard")
    public ResponseEntity<List<CardNumberResponse>> getCardBlackList() {
        return ResponseEntity.ok(antiFraudService.getBlackListCardNumbers());
    }

    @PostMapping("/transaction")
    public ResponseEntity<TransactionResponse> sendTx(@RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.processTx(transactionRequest));
    }


}
