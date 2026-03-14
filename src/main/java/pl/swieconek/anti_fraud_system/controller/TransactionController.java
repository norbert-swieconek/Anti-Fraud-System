package pl.swieconek.anti_fraud_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.swieconek.anti_fraud_system.dto.RequestTx;
import pl.swieconek.anti_fraud_system.service.TransactionService;
import pl.swieconek.anti_fraud_system.dto.ResponseTx;

@RestController
@RequestMapping("/api/antifraud")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<ResponseTx> sendTx(@RequestBody RequestTx requestTx) {
        return ResponseEntity.ok(transactionService.procesTx(requestTx));
    }


}
