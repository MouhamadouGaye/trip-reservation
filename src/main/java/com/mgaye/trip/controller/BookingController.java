package com.mgaye.trip.controller;

import com.mgaye.trip.dto.BookingCreateDto;
import com.mgaye.trip.entity.Booking;
import com.mgaye.trip.entity.User;
import com.mgaye.trip.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingCreateDto dto,
            @AuthenticationPrincipal User user) {
        try {
            Booking booking = bookingService.createBooking(dto, user);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getUserBookings(@AuthenticationPrincipal User user) {
        List<Booking> bookings = bookingService.findUserBookings(user.getId());
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{referenceNumber}")
    public ResponseEntity<?> getBookingByReference(@PathVariable String referenceNumber) {
        return bookingService.findByReferenceNumber(referenceNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id,
            @AuthenticationPrincipal User user) {
        try {
            Booking booking = bookingService.cancelBooking(id, user.getId());
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}