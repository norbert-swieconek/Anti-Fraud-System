package pl.swieconek.anti_fraud_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TransactionRequest(@NotNull Long amount, @NotBlank String  ip, @NotBlank String number, @NotBlank String region, @NotNull LocalDateTime date) {
}
