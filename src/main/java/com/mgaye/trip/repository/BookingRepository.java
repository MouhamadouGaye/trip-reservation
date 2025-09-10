package com.mgaye.trip.repository;

import com.mgaye.trip.entity.Booking;
import com.mgaye.trip.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByReferenceNumber(String referenceNumber);

    List<Booking> findByUserIdOrderByBookingDateDesc(Long userId);

    @Query("SELECT b.seatNumber FROM Booking b WHERE b.trip.id = :tripId AND b.status = 'CONFIRMED'")
    List<String> findBookedSeatsByTripId(@Param("tripId") Long tripId);

    List<Booking> findByEmailOrderByBookingDateDesc(String email);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = :status")
    Long countByStatus(@Param("status") BookingStatus status);
}