package hajiton.chaebee.domain.discovery.controller;

import hajiton.chaebee.domain.discovery.entity.SubDiscovery;
import hajiton.chaebee.domain.dto.ApiResponse;
import hajiton.chaebee.domain.discovery.service.DiscoveryService;
import hajiton.chaebee.domain.trip.entity.Tag;
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
        var response = discoveryService.getDiscoveries(countryCode, tripType, page, size);
        return ApiResponse.success(response);
    }

    @GetMapping("/{discoveryId}")
    public ApiResponse<?> getDiscovery(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long discoveryId) {
        var response = discoveryService.getDiscovery(memberId, discoveryId);
        return ApiResponse.success(response);
    }

    public record DiscoveryCreateRequest(Long tripId, String tripType, List<DiscoveryService.SubDiscoveryRequest> subDiscoveries) {}

    public record SubDiscoveryCardResponse(
            Long subDiscoveryId,
            Tag tag,
            String content
    ) {
        public static SubDiscoveryCardResponse from(SubDiscovery subDiscovery) {
            return new SubDiscoveryCardResponse(
                    subDiscovery.getId(),
                    subDiscovery.getTag(),
                    subDiscovery.getContent()
            );
        }
    }
}

