package pl.swieconek.anti_fraud_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.swieconek.anti_fraud_system.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByNumberAndDateGreaterThanAndDateLessThan(String number, LocalDateTime startDate, LocalDateTime endDate);
}
