package pl.swieconek.anti_fraud_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.swieconek.anti_fraud_system.model.StolenCard;

import java.util.Optional;

public interface StolenCardRepository extends JpaRepository<StolenCard, Long> {
    Optional<StolenCard> findByNumber(String number);
}
