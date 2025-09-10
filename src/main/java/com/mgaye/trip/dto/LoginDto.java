package com.mgaye.trip.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class LoginDto {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}