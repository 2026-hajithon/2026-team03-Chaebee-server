package hajiton.chaebee.domain.discovery.controller;

import hajiton.chaebee.domain.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sub-discoveries")
public class SubDiscoveryController {

    @GetMapping
    public ApiResponse<?> getSubDiscoveries(
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) String tag) {

        return ApiResponse.success(null);
    }

    @GetMapping("/daily")
    public ApiResponse<?> getDailySubDiscoveries() {

        return ApiResponse.success(null);
    }
}
