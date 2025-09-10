package com.mgaye.trip.service;

import com.mgaye.trip.dto.BookingCreateDto;
import com.mgaye.trip.entity.Booking;
import com.mgaye.trip.entity.BookingStatus;
import com.mgaye.trip.entity.Trip;
import com.mgaye.trip.entity.User;
import com.mgaye.trip.repository.BookingRepository;
import com.mgaye.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;

    public Booking createBooking(BookingCreateDto dto, User user) {
        Trip trip = tripRepository.findById(dto.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        // Check seat availability
        if (trip.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available");
        }

        // Check if seat is already booked
        List<String> bookedSeats = bookingRepository.findBookedSeatsByTripId(trip.getId());
        if (bookedSeats.contains(dto.getSeatNumber())) {
            throw new RuntimeException("Seat already booked");
        }

        // Calculate current price
        BigDecimal currentPrice = calculateDynamicPrice(trip, trip.getDepartureDate());

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTrip(trip);
        booking.setPassengerName(dto.getPassengerName());
        booking.setEmail(dto.getEmail());
        booking.setPhone(dto.getPhone());
        booking.setSeatNumber(dto.getSeatNumber());
        booking.setPrice(currentPrice);
        booking.setSpecialRequests(dto.getSpecialRequests());
        booking.setNewsletterSubscription(dto.getNewsletterSubscription());
        booking.setReferenceNumber(generateReferenceNumber());

        // Update trip availability
        trip.setAvailableSeats(trip.getAvailableSeats() - 1);
        tripRepository.save(trip);

        return bookingRepository.save(booking);
    }

    public Optional<Booking> findByReferenceNumber(String referenceNumber) {
        return bookingRepository.findByReferenceNumber(referenceNumber);
    }

    public List<Booking> findUserBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByBookingDateDesc(userId);
    }

    public List<Booking> findBookingsByEmail(String email) {
        return bookingRepository.findByEmailOrderByBookingDateDesc(email);
    }

    public List<String> getBookedSeats(Long tripId) {
        return bookingRepository.findBookedSeatsByTripId(tripId);
    }

    public Booking cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking cannot be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        // Increase trip availability
        Trip trip = booking.getTrip();
        trip.setAvailableSeats(trip.getAvailableSeats() + 1);
        tripRepository.save(trip);

        return bookingRepository.save(booking);
    }

    private String generateReferenceNumber() {
        return "TRP" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private BigDecimal calculateDynamicPrice(Trip trip, LocalDate requestedDate) {
        // Same logic as TripService
        LocalDate today = LocalDate.now();
        long daysUntilTrip = ChronoUnit.DAYS.between(today, requestedDate);

        BigDecimal multiplier = BigDecimal.ONE;

        if (daysUntilTrip < 3) {
            multiplier = multiplier.add(BigDecimal.valueOf(0.3));
        } else if (daysUntilTrip < 7) {
            multiplier = multiplier.add(BigDecimal.valueOf(0.15));
        } else if (daysUntilTrip > 30) {
            multiplier = multiplier.subtract(BigDecimal.valueOf(0.1));
        }

        int dayOfWeek = requestedDate.getDayOfWeek().getValue();
        if (dayOfWeek >= 5) {
            multiplier = multiplier.add(BigDecimal.valueOf(0.2));
        }

        multiplier = multiplier.add(trip.getPriceVariation().divide(BigDecimal.valueOf(100)));

        return trip.getBasePrice().multiply(multiplier).setScale(0, BigDecimal.ROUND_HALF_UP);
    }
}
