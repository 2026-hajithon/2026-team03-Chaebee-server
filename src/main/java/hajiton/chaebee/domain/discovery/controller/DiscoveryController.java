package hajiton.chaebee.domain.discovery.controller;

import hajiton.chaebee.common.dto.ApiResponse;
import hajiton.chaebee.domain.discovery.service.DiscoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/discoveries")
@RequiredArgsConstructor
public class DiscoveryController {

    private final DiscoveryService discoveryService;

    @PostMapping
    public ApiResponse<?> createDiscovery(
            @AuthenticationPrincipal Long memberId,
            @RequestBody DiscoveryCreateRequest request) {

        var response = discoveryService.createDiscovery(
                memberId,
                request.tripId(),
                request.tripType(),
                request.subDiscoveries()
        );
        return ApiResponse.success(response);
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