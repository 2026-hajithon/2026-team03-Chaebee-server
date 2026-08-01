package hajiton.chaebee.discovery.controller;

import hajiton.chaebee.common.dto.ApiResponse;
import hajiton.chaebee.discovery.service.DiscoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/discoveries")
@RequiredArgsConstructor
public class DiscoveryController {

    private final DiscoveryService discoveryService;

    @PostMapping
    public ApiResponse<?> createDiscovery(@RequestBody DiscoveryCreateRequest request) {
        Long dummyMemberId = 1L; // TODO: 시큐리티나 인터셉터에서 추출한 memberId 사용
        
        var response = discoveryService.createDiscovery(
                dummyMemberId,
                request.tripId(),
                request.tripType(),
                request.subDiscoveries()
        );
        return ApiResponse.success(response);
    }

    @GetMapping
    public ApiResponse<?> getDiscoveries(
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) String tripType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // TODO: 발견 목록 조회 (최신순) 로직
        return ApiResponse.success(null);
    }

    @GetMapping("/{discoveryId}")
    public ApiResponse<?> getDiscovery(@PathVariable Long discoveryId) {
        // TODO: 발견 상세 조회 로직
        return ApiResponse.success(null);
    }

    public record DiscoveryCreateRequest(Long tripId, String tripType, List<DiscoveryService.SubDiscoveryRequest> subDiscoveries) {}
}
