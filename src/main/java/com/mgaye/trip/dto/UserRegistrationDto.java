package com.mgaye.trip.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class UserRegistrationDto {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[\\d\\s\\-()]+$", message = "Invalid phone format")
    @NotBlank(message = "Phone is required")
    private String phone;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "Password is required")
    private String password;

    private String vehicleType = "ALL";
    private Boolean notifications = true;
}
