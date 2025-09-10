package com.mgaye.trip.dto.response;

import lombok.Data;
import lombok.Builder;
import com.mgaye.trip.entity.VehicleType;
import com.mgaye.trip.entity.Amenity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class TripResponseDto {
    private Long id;
    private VehicleType type;
    private String from;
    private String to;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalDate arrivalDate;
    private LocalTime arrivalTime;
    private Integer duration;
    private BigDecimal basePrice;
    private BigDecimal currentPrice;
    private Integer totalSeats;
    private Integer availableSeats;
    private String carrier;
    private Set<Amenity> amenities;
    private LocalDateTime dealEndsAt;
    private Integer discount;
}