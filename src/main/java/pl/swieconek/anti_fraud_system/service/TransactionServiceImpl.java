package pl.swieconek.anti_fraud_system.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.swieconek.anti_fraud_system.dto.Status;
import pl.swieconek.anti_fraud_system.dto.RequestTx;
import pl.swieconek.anti_fraud_system.dto.ResponseTx;

@Service
public class TransactionServiceImpl implements TransactionService {

    public ResponseTx processTx(RequestTx requestTx) {
        if (requestTx.amount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (requestTx.amount() <= 200) {
            return new ResponseTx(Status.ALLOWED.toString());
        } else if (requestTx.amount() <= 1500) {
            return new ResponseTx(Status.MANUAL_PROCESSING.toString());
        } else {
            return new ResponseTx(Status.PROHIBITED.toString());
        }
    }
}
