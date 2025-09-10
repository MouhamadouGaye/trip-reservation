package com.mgaye.trip.repository;

import com.mgaye.trip.entity.Trip;
import com.mgaye.trip.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("SELECT t FROM Trip t WHERE " +
            "t.from = :from AND t.to = :to AND " +
            "t.departureDate = :departureDate AND " +
            "(:vehicleType = 'ALL' OR t.type = :vehicleType) AND " +
            "t.availableSeats > 0")
    List<Trip> findTrips(@Param("from") String from,
            @Param("to") String to,
            @Param("departureDate") LocalDate departureDate,
            @Param("vehicleType") VehicleType vehicleType);

    @Query("SELECT t FROM Trip t WHERE " +
            "t.from = :from AND t.to = :to AND " +
            "t.departureDate BETWEEN :startDate AND :endDate AND " +
            "(:vehicleType = 'ALL' OR t.type = :vehicleType) AND " +
            "t.availableSeats > 0")
    List<Trip> findFlexibleTrips(@Param("from") String from,
            @Param("to") String to,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("vehicleType") VehicleType vehicleType);

    @Query("SELECT DISTINCT t.from FROM Trip t ORDER BY t.from")
    List<String> findAllOrigins();

    @Query("SELECT DISTINCT t.to FROM Trip t ORDER BY t.to")
    List<String> findAllDestinations();
}