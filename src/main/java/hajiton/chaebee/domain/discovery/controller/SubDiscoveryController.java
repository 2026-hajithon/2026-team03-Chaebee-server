package hajiton.chaebee.domain.discovery.controller;

import hajiton.chaebee.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sub-discoveries")
public class SubDiscoveryController {

    @GetMapping
    public ApiResponse<?> getSubDiscoveries(
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) String tag) {
        // TODO: 서브발견 조회 (타임라인 카드용) 로직
        return ApiResponse.success(null);
    }

    @GetMapping("/daily")
    public ApiResponse<?> getDailySubDiscoveries() {
        // TODO: 오늘의 서브발견 (하루 1회 상태 갱신) 로직
        return ApiResponse.success(null);
    }
}
