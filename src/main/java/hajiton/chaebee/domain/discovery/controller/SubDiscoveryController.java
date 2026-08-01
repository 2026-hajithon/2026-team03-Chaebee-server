package hajiton.chaebee.domain.discovery.controller;

import hajiton.chaebee.domain.discovery.entity.SubDiscovery;
import hajiton.chaebee.domain.discovery.service.SubDiscoveryService;
import hajiton.chaebee.domain.common.dto.ApiResponse;
import hajiton.chaebee.domain.trip.entity.Tag;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@io.swagger.v3.oas.annotations.tags.Tag(name = "SubDiscovery API", description = "서브 발견 관련 API")
@RestController
@RequestMapping("/api/sub-discoveries")
@RequiredArgsConstructor
public class SubDiscoveryController {

    private final SubDiscoveryService subDiscoveryService;

    @Operation(summary = "서브 발견 조회", description = "조건에 맞는 서브 발견 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getSubDiscoveries(
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) String tag) {
        var response = subDiscoveryService.getSubDiscoveries(countryCode, tag);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "일일 서브 발견 조회", description = "매일 새롭게 배정되는 서브 발견을 조회합니다.")
    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<?>> getDailySubDiscoveries(@AuthenticationPrincipal Long memberId) {
        var response = subDiscoveryService.getDailySubDiscoveries(memberId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
