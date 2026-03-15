package pl.swieconek.anti_fraud_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.swieconek.anti_fraud_system.model.SuspiciousIp;

import java.util.Optional;

public interface SuspiciousIpRepository extends JpaRepository<SuspiciousIp, Long> {
    Optional<SuspiciousIp> findByIp(String ip);
}
