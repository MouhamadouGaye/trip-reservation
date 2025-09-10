package com.mgaye.trip.dto;

import lombok.Data;
import com.mgaye.trip.entity.VehicleType;

import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Data
public class TripSearchDto {
    @NotBlank(message = "Origin is required")
    private String from;

    @NotBlank(message = "Destination is required")
    private String to;

    @NotNull(message = "Departure date is required")
    @Future(message = "Departure date must be in the future")
    private LocalDate departureDate;

    private LocalDate returnDate;

    @Min(value = 1, message = "At least 1 passenger required")
    @Max(value = 8, message = "Maximum 8 passengers allowed")
    private Integer passengers = 1;

    private VehicleType vehicleType = VehicleType.ALL;
    private Integer maxPrice = 300;
    private Integer maxDuration = 24;
    private String sortBy = "price";
    private Boolean flexibleDates = false;
}