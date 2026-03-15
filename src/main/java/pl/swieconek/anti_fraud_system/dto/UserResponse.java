package pl.swieconek.anti_fraud_system.dto;

import pl.swieconek.anti_fraud_system.model.Role;

public record UserResponse (Long id, String name, String username, Role role) {
}
