package hajiton.chaebee.domain.discovery.controller;

import hajiton.chaebee.domain.discovery.dto.DiscoveryReq;
import hajiton.chaebee.domain.discovery.dto.DiscoveryRes;
import hajiton.chaebee.domain.dto.ApiResponse;
import hajiton.chaebee.domain.discovery.service.DiscoveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/discoveries")
@RequiredArgsConstructor
public class DiscoveryController {

    private final DiscoveryService discoveryService;


    //발견 등록
    @PostMapping
    public ResponseEntity<ApiResponse<DiscoveryRes.DiscoveryResponse>> createDiscovery(
            @AuthenticationPrincipal Long memberId,
            @RequestBody @Valid DiscoveryReq.CreateDiscoveryRequest request
    ) {
        DiscoveryRes.DiscoveryResponse response = discoveryService.createDiscovery(memberId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    //타임라인 구성
    @GetMapping("/trips/{tripId}/timeline")
    public ResponseEntity<DiscoveryRes.TimelineResponse> getTimeline(@PathVariable Long tripId) {
        DiscoveryRes.TimelineResponse response = discoveryService.getTimeline(tripId);
        return ResponseEntity.ok(response);
    }




    @GetMapping
    public ApiResponse<?> getDiscoveries(
            @AuthenticationPrincipal Long memberId,
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) String tripType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // TODO: 발견 목록 조회 (최신순) 로직 - memberId 활용
        return ApiResponse.success(null);
    }

    @GetMapping("/{discoveryId}")
    public ApiResponse<?> getDiscovery(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long discoveryId) {
        // TODO: 발견 상세 조회 로직 - memberId 활용
        return ApiResponse.success(null);
    }

    public record DiscoveryCreateRequest(Long tripId, String tripType, List<DiscoveryService.SubDiscoveryRequest> subDiscoveries) {}
}