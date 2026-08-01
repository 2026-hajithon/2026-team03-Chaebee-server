package hajiton.chaebee.domain.discovery.controller;

import hajiton.chaebee.domain.discovery.dto.DiscoveryReq;
import hajiton.chaebee.domain.discovery.dto.DiscoveryRes;
import hajiton.chaebee.domain.common.dto.ApiResponse;
import hajiton.chaebee.domain.discovery.service.DiscoveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

@io.swagger.v3.oas.annotations.tags.Tag(name = "Discovery API", description = "발견 관련 API")
@RestController
@RequestMapping("/api/discoveries")
@RequiredArgsConstructor
public class DiscoveryController {

    private final DiscoveryService discoveryService;

    @Operation(summary = "발견 등록", description = "여행의 발견을 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<DiscoveryRes.DiscoveryResponse>> createDiscovery(
            @AuthenticationPrincipal Long memberId,
            @RequestBody @Valid DiscoveryReq.CreateDiscoveryRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(discoveryService.createDiscovery(memberId, request)));
    }

    @Operation(summary = "발견 목록 조회", description = "가장 최근에 등록된 발견 10개를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<DiscoveryRes.DiscoveryListResponse>> getDiscoveries(
            @AuthenticationPrincipal Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var response = discoveryService.getDiscoveries(page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내 발견 목록 조회", description = "내가 등록한 모든 발견 목록을 최신순으로 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<java.util.List<DiscoveryRes.DiscoveryListItemResponse>>> getMyDiscoveries(
            @AuthenticationPrincipal Long memberId) {
        var response = discoveryService.getMyDiscoveries(memberId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /*@Operation(summary = "발견 상세 조회", description = "특정 발견의 상세 정보를 조회합니다.")
    @GetMapping("/{discoveryId}")
    public ResponseEntity<ApiResponse<?>> getDiscovery(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long discoveryId) {
        var response = discoveryService.getDiscovery(memberId, discoveryId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }*/
}