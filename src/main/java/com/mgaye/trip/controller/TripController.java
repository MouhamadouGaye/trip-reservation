package com.mgaye.trip.controller;

import com.mgaye.trip.dto.response.TripResponseDto;
import com.mgaye.trip.dto.TripSearchDto;
import com.mgaye.trip.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping("/search")
    public ResponseEntity<List<TripResponseDto>> searchTrips(@Valid @RequestBody TripSearchDto searchDto) {
        List<TripResponseDto> trips = tripService.searchTrips(searchDto);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/origins")
    public ResponseEntity<List<String>> getOrigins() {
        return ResponseEntity.ok(tripService.getAllOrigins());
    }

    @GetMapping("/destinations")
    public ResponseEntity<List<String>> getDestinations() {
        return ResponseEntity.ok(tripService.getAllDestinations());
    }
}
