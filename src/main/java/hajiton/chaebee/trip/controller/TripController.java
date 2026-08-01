package hajiton.chaebee.trip.controller;

import hajiton.chaebee.common.dto.ApiResponse;
import hajiton.chaebee.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ApiResponse<?> createTrip(@RequestBody TripCreateRequest request) {
        // TODO: 인터셉터나 시큐리티에서 추출한 memberId를 넘겨주도록 수정 필요
        Long dummyMemberId = 1L; 
        
        var response = tripService.createTrip(
                dummyMemberId,
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
    public ApiResponse<?> getMyTrip() {
        Long dummyMemberId = 1L; 
        var response = tripService.getMyTrip(dummyMemberId);
        return ApiResponse.success(response);
    }

    @GetMapping("/{tripId}")
    public ApiResponse<?> getTrip(@PathVariable Long tripId) {
        Long dummyMemberId = 1L; 
        var response = tripService.getTrip(dummyMemberId, tripId);
        return ApiResponse.success(response);
    }

    @PatchMapping("/{tripId}")
    public ApiResponse<?> updateTrip(@PathVariable Long tripId, @RequestBody Object request) {
        Long dummyMemberId = 1L; 
        tripService.updateTrip(dummyMemberId, tripId, request);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{tripId}")
    public ApiResponse<?> deleteTrip(@PathVariable Long tripId) {
        Long dummyMemberId = 1L; 
        tripService.deleteTrip(dummyMemberId, tripId);
        return ApiResponse.success(null);
    }

    @GetMapping("/{tripId}/timeline")
    public ApiResponse<?> getTimeline(@PathVariable Long tripId) {
        Long dummyMemberId = 1L; 
        var response = tripService.getTimeline(dummyMemberId, tripId);
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
