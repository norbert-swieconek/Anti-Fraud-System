package pl.swieconek.anti_fraud_system.service;

import pl.swieconek.anti_fraud_system.dto.RequestTx;
import pl.swieconek.anti_fraud_system.dto.ResponseTx;

public interface TransactionService {
    ResponseTx processTx(RequestTx requestTx);
}
