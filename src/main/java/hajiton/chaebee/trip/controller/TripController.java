package hajiton.chaebee.trip.controller;

import hajiton.chaebee.common.dto.ApiResponse;
import hajiton.chaebee.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ApiResponse<?> createTrip(
            @AuthenticationPrincipal Long memberId,
            @RequestBody TripCreateRequest request) {

        var response = tripService.createTrip(
                memberId,
                request.countryCode(),
                request.cityCode(),
                request.departureAt(),
                request.arrivalAt(),
                request.esimPlan(),
                request.cashPlan()
        );
        return ApiResponse.success(response);
    }

    @GetMapping("/me")
    public ApiResponse<?> getMyTrip(@AuthenticationPrincipal Long memberId) {
        var response = tripService.getMyTrip(memberId);
        return ApiResponse.success(response);
    }

    @GetMapping("/{tripId}")
    public ApiResponse<?> getTrip(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long tripId) {
        var response = tripService.getTrip(memberId, tripId);
        return ApiResponse.success(response);
    }

    @PatchMapping("/{tripId}")
    public ApiResponse<?> updateTrip(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long tripId,
            @RequestBody Object request) {
        tripService.updateTrip(memberId, tripId, request);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{tripId}")
    public ApiResponse<?> deleteTrip(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long tripId) {
        tripService.deleteTrip(memberId, tripId);
        return ApiResponse.success(null);
    }

    @GetMapping("/{tripId}/timeline")
    public ApiResponse<?> getTimeline(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long tripId) {
        var response = tripService.getTimeline(memberId, tripId);
        return ApiResponse.success(response);
    }

    public record TripCreateRequest(
            String countryCode,
            String cityCode,
            LocalDateTime departureAt,
            LocalDateTime arrivalAt,
            Boolean esimPlan,
            Boolean cashPlan
    ) {}
}