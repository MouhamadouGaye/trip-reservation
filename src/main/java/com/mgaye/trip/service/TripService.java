package com.mgaye.trip.service;

import com.mgaye.trip.dto.TripSearchDto;
import com.mgaye.trip.dto.response.TripResponseDto;
import com.mgaye.trip.entity.Trip;
import com.mgaye.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    public List<TripResponseDto> searchTrips(TripSearchDto searchDto) {
        List<Trip> trips;

        if (searchDto.getFlexibleDates()) {
            LocalDate startDate = searchDto.getDepartureDate().minusDays(3);
            LocalDate endDate = searchDto.getDepartureDate().plusDays(3);
            trips = tripRepository.findFlexibleTrips(
                    searchDto.getFrom(),
                    searchDto.getTo(),
                    startDate,
                    endDate,
                    searchDto.getVehicleType());
        } else {
            trips = tripRepository.findTrips(
                    searchDto.getFrom(),
                    searchDto.getTo(),
                    searchDto.getDepartureDate(),
                    searchDto.getVehicleType());
        }

        return trips.stream()
                .map(trip -> convertToDto(trip, searchDto.getDepartureDate()))
                .filter(dto -> dto.getCurrentPrice().compareTo(BigDecimal.valueOf(searchDto.getMaxPrice())) <= 0)
                .filter(dto -> dto.getDuration() <= searchDto.getMaxDuration() * 60)
                .sorted((a, b) -> {
                    switch (searchDto.getSortBy()) {
                        case "price":
                            return a.getCurrentPrice().compareTo(b.getCurrentPrice());
                        case "duration":
                            return a.getDuration().compareTo(b.getDuration());
                        case "departure":
                            return a.getDepartureTime().compareTo(b.getDepartureTime());
                        default:
                            return 0;
                    }
                })
                .collect(Collectors.toList());
    }

    public Optional<Trip> findById(Long id) {
        return tripRepository.findById(id);
    }

    public List<String> getAllOrigins() {
        return tripRepository.findAllOrigins();
    }

    public List<String> getAllDestinations() {
        return tripRepository.findAllDestinations();
    }

    private TripResponseDto convertToDto(Trip trip, LocalDate requestedDate) {
        BigDecimal currentPrice = calculateDynamicPrice(trip, requestedDate);
        Integer discount = calculateDiscount(trip.getBasePrice(), currentPrice);

        return TripResponseDto.builder()
                .id(trip.getId())
                .type(trip.getType())
                .from(trip.getFrom())
                .to(trip.getTo())
                .departureDate(trip.getDepartureDate())
                .departureTime(trip.getDepartureTime())
                .arrivalDate(trip.getArrivalDate())
                .arrivalTime(trip.getArrivalTime())
                .duration(trip.getDuration())
                .basePrice(trip.getBasePrice())
                .currentPrice(currentPrice)
                .totalSeats(trip.getTotalSeats())
                .availableSeats(trip.getAvailableSeats())
                .carrier(trip.getCarrier())
                .amenities(trip.getAmenities())
                .dealEndsAt(trip.getDealEndsAt())
                .discount(discount)
                .build();
    }

    private BigDecimal calculateDynamicPrice(Trip trip, LocalDate requestedDate) {
        LocalDate today = LocalDate.now();
        long daysUntilTrip = ChronoUnit.DAYS.between(today, requestedDate);

        BigDecimal multiplier = BigDecimal.ONE;

        // Price increases as date approaches
        if (daysUntilTrip < 3) {
            multiplier = multiplier.add(BigDecimal.valueOf(0.3));
        } else if (daysUntilTrip < 7) {
            multiplier = multiplier.add(BigDecimal.valueOf(0.15));
        } else if (daysUntilTrip > 30) {
            multiplier = multiplier.subtract(BigDecimal.valueOf(0.1));
        }

        // Weekend premium
        int dayOfWeek = requestedDate.getDayOfWeek().getValue();
        if (dayOfWeek >= 5) { // Friday, Saturday, Sunday
            multiplier = multiplier.add(BigDecimal.valueOf(0.2));
        }

        // Market variation
        multiplier = multiplier.add(trip.getPriceVariation().divide(BigDecimal.valueOf(100)));

        return trip.getBasePrice().multiply(multiplier).setScale(0, RoundingMode.HALF_UP);
    }

    private Integer calculateDiscount(BigDecimal basePrice, BigDecimal currentPrice) {
        if (currentPrice.compareTo(basePrice) < 0) {
            BigDecimal discount = basePrice.subtract(currentPrice)
                    .divide(basePrice, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            return discount.intValue();
        }
        return 0;
    }
}
