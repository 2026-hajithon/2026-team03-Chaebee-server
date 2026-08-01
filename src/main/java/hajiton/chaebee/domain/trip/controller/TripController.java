package hajiton.chaebee.domain.trip.controller;

import hajiton.chaebee.domain.common.dto.ApiResponse;
import hajiton.chaebee.domain.trip.dto.TripReq;
import hajiton.chaebee.domain.trip.service.TripService;
import lombok.RequiredArgsConstructor;
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

    //여행 등록
    @Operation(summary = "여행 등록", description = "새로운 여행을 등록합니다.")
    @PostMapping
    public ApiResponse<?> createTrip(
            @AuthenticationPrincipal Long memberId,
            @RequestBody TripReq.TripCreateRequest request) {

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

    @Operation(summary = "내 여행 조회", description = "나의 여행 목록을 조회합니다.")
    @GetMapping("/me")
    public ApiResponse<?> getMyTrip(@AuthenticationPrincipal Long memberId) {
        var response = tripService.getMyTrip(memberId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "여행 상세 조회", description = "특정 여행의 상세 정보를 조회합니다.")
    @GetMapping("/{tripId}")
    public ApiResponse<?> getTrip(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long tripId) {
        var response = tripService.getTrip(memberId, tripId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "여행 수정", description = "특정 여행의 정보를 수정합니다.")
    @PatchMapping("/{tripId}")
    public ApiResponse<?> updateTrip(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long tripId,
            @RequestBody Object request) {
        tripService.updateTrip(memberId, tripId, request);
        return ApiResponse.success(null);
    }

    @Operation(summary = "여행 삭제", description = "특정 여행을 삭제합니다.")
    @DeleteMapping("/{tripId}")
    public ApiResponse<?> deleteTrip(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long tripId) {
        tripService.deleteTrip(memberId, tripId);
        return ApiResponse.success(null);
    }

    @Operation(summary = "여행 타임라인 조회", description = "특정 여행의 타임라인을 조회합니다.")
    @GetMapping("/{tripId}/timeline")
    public ApiResponse<?> getTimeline(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long tripId) {
        var response = tripService.getTimeline(memberId, tripId);
        return ApiResponse.success(response);
    }

}