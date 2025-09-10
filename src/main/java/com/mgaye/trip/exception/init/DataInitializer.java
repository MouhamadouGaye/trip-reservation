package com.mgaye.trip.exception.init;

import com.mgaye.trip.entity.Amenity;
import com.mgaye.trip.entity.Trip;
import com.mgaye.trip.entity.VehicleType;
import com.mgaye.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final TripRepository tripRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Sample trips data
            Trip trip1 = new Trip();
            trip1.setType(VehicleType.TRAIN);
            trip1.setFrom("New York");
            trip1.setTo("Boston");
            trip1.setDepartureDate(LocalDate.now().plusDays(5));
            trip1.setDepartureTime(LocalTime.of(8, 0));
            trip1.setArrivalDate(LocalDate.now().plusDays(5));
            trip1.setArrivalTime(LocalTime.of(12, 30));
            trip1.setDuration(270);
            trip1.setBasePrice(new BigDecimal("89.99"));
            trip1.setTotalSeats(100);
            trip1.setAvailableSeats(85);
            trip1.setCarrier("Amtrak");
            trip1.setAmenities(Set.of(Amenity.WIFI, Amenity.POWER));
            trip1.setDealEndsAt(LocalDateTime.now().plusHours(48));
            trip1.setPriceVariation(new BigDecimal("5.00"));

            Trip trip2 = new Trip();
            trip2.setType(VehicleType.BUS);
            trip2.setFrom("New York");
            trip2.setTo("Washington DC");
            trip2.setDepartureDate(LocalDate.now().plusDays(3));
            trip2.setDepartureTime(LocalTime.of(10, 0));
            trip2.setArrivalDate(LocalDate.now().plusDays(3));
            trip2.setArrivalTime(LocalTime.of(16, 0));
            trip2.setDuration(360);
            trip2.setBasePrice(new BigDecimal("49.99"));
            trip2.setTotalSeats(50);
            trip2.setAvailableSeats(30);
            trip2.setCarrier("Greyhound");
            trip2.setAmenities(Set.of(Amenity.WIFI));
            trip2.setPriceVariation(new BigDecimal("-2.50"));

            tripRepository.save(trip1);
            tripRepository.save(trip2);
        };
    }
}