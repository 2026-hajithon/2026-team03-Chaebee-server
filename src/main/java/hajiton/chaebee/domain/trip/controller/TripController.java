package hajiton.chaebee.domain.trip.controller;

import hajiton.chaebee.domain.common.dto.ApiResponse;
import hajiton.chaebee.domain.trip.dto.TripReq;
import hajiton.chaebee.domain.trip.dto.TripRes.TripResponse;
import hajiton.chaebee.domain.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "Trip API", description = "여행 관련 API")
@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @Operation(summary = "여행 등록", description = "새로운 여행을 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<TripResponse>> createTrip(
            @AuthenticationPrincipal Long memberId,
            @RequestBody TripReq.TripCreateRequest request) {

        TripResponse response = tripService.createTrip(
                memberId,
                request.countryCode(),
                request.cityCode(),
                request.departureAt(),
                request.arrivalAt(),
                request.esimPlan(),
                request.cashPlan()
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내 모든 여행 조회", description = "내가 등록한 모든 여행 목록을 최신순으로 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<java.util.List<TripResponse>>> getMyTrip(@AuthenticationPrincipal Long memberId) {
        var response = tripService.getMyTrip(memberId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "여행 삭제", description = "특정 여행을 삭제합니다.")
    @DeleteMapping("/{tripId}")
    public ResponseEntity<ApiResponse<Void>> deleteTrip(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long tripId) {
        tripService.deleteTrip(memberId, tripId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "여행 타임라인 조회", description = "특정 여행의 타임라인을 조회합니다.")
    @GetMapping("/{tripId}/timeline")
    public ResponseEntity<ApiResponse<hajiton.chaebee.domain.trip.dto.TripRes.TimelineResponse>> getTimeline(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long tripId) {
        var response = tripService.getTimeline(memberId, tripId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}