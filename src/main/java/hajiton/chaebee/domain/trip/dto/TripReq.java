package hajiton.chaebee.domain.trip.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public class TripReq {

    private TripReq() {}

    @Schema(description = "여행 생성 요청 DTO")
    public record TripCreateRequest(
            @Schema(description = "국가 코드", example = "JAPAN")
            String countryCode,
            @Schema(description = "도시 코드", example = "TOKYO")
            String cityCode,
            @Schema(description = "출발 일시", example = "2026-09-10T09:00:00")
            LocalDateTime departureAt,
            @Schema(description = "도착 일시", example = "2026-09-20T18:00:00")
            LocalDateTime arrivalAt,
            @Schema(description = "eSIM 계획 여부", example = "true")
            Boolean esimPlan,
            @Schema(description = "현금 계획 여부", example = "false")
            Boolean cashPlan
    ) {}
}
