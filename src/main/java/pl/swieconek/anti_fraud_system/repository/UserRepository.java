package pl.swieconek.anti_fraud_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.swieconek.anti_fraud_system.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
