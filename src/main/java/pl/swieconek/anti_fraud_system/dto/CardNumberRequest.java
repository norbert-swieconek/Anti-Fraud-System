package pl.swieconek.anti_fraud_system.dto;

import jakarta.validation.constraints.NotBlank;

public record CardNumberRequest(@NotBlank String number) {
}
