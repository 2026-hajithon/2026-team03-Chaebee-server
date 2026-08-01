package hajiton.chaebee.domain.discovery.controller;

import hajiton.chaebee.domain.discovery.dto.DiscoveryReq;
import hajiton.chaebee.domain.discovery.dto.DiscoveryRes;
import hajiton.chaebee.domain.discovery.entity.SubDiscovery;
import hajiton.chaebee.domain.dto.ApiResponse;
import hajiton.chaebee.domain.discovery.service.DiscoveryService;
import jakarta.validation.Valid;
import hajiton.chaebee.domain.trip.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;

@io.swagger.v3.oas.annotations.tags.Tag(name = "Discovery API", description = "발견 관련 API")
@RestController
@RequestMapping("/api/discoveries")
@RequiredArgsConstructor
public class DiscoveryController {

    private final DiscoveryService discoveryService;


    // 발견 등록
    @Operation(summary = "발견 등록", description = "여행의 발견을 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<DiscoveryRes.DiscoveryResponse>> createDiscovery(
            @AuthenticationPrincipal Long memberId,
            @RequestBody @Valid DiscoveryReq.CreateDiscoveryRequest request
    ) {
        DiscoveryRes.DiscoveryResponse response = discoveryService.createDiscovery(memberId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    // 타임라인 구성 (ApiResponse 적용)
    @Operation(summary = "타임라인 조회", description = "특정 여행의 타임라인을 조회합니다.")
    @GetMapping("/trips/{tripId}/timeline")
    public ResponseEntity<ApiResponse<DiscoveryRes.TimelineResponse>> getTimeline(@PathVariable Long tripId) {
        DiscoveryRes.TimelineResponse response = discoveryService.getTimeline(tripId);
        return ResponseEntity.ok(ApiResponse.success(response)); // 💡 여기서 한 번 감싸주기만 하면 끝!
    }




    @Operation(summary = "발견 목록 조회", description = "조건에 맞는 발견 목록을 조회합니다.")
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

    @Operation(summary = "발견 상세 조회", description = "특정 발견의 상세 정보를 조회합니다.")
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