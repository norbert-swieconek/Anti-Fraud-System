package pl.swieconek.anti_fraud_system.service;

import pl.swieconek.anti_fraud_system.dto.TransactionRequest;
import pl.swieconek.anti_fraud_system.dto.TransactionResponse;

public interface TransactionService {
    TransactionResponse processTx(TransactionRequest transactionRequest);
}
