package pl.swieconek.anti_fraud_system.dto;


import jakarta.validation.constraints.NotBlank;

public record IpRequest (@NotBlank String ip) {
}
