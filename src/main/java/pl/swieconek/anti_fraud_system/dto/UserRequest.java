package pl.swieconek.anti_fraud_system.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequest (@NotBlank String name, @NotBlank String username, @NotBlank String password){

}
