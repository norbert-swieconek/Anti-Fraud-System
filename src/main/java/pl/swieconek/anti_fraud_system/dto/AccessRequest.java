package pl.swieconek.anti_fraud_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.swieconek.anti_fraud_system.model.Operation;

public record AccessRequest(@NotBlank String username, @NotNull Operation operation) {
}
