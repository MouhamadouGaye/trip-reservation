package com.mgaye.trip.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class BookingCreateDto {
    @NotNull(message = "Trip ID is required")
    private Long tripId;

    @NotBlank(message = "Passenger name is required")
    private String passengerName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[\\d\\s\\-()]+$", message = "Invalid phone format")
    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    private String specialRequests;
    private Boolean newsletterSubscription = false;
}
