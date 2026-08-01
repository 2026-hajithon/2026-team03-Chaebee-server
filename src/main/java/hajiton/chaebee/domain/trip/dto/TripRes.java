package hajiton.chaebee.domain.trip.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public class TripRes {

    @Schema(description = "여행 응답 DTO")
    public record TripResponse(
            @Schema(description = "여행 ID", example = "1")
            Long tripId,

            @Schema(description = "국가 코드", example = "USA")
            String countryCode,

            @Schema(description = "도시 코드", example = "LOS_ANGELES")
            String cityCode,

            @Schema(description = "출발일", example = "2026-09-10T09:00:00")
            LocalDateTime departureAt,

            @Schema(description = "도착일", example = "2026-09-20T18:00:00")
            LocalDateTime arrivalAt,

            @Schema(description = "eSIM 계획 여부", example = "true")
            Boolean esimPlan,

            @Schema(description = "현금 계획 여부", example = "false")
            Boolean cashPlan,

            @Schema(description = "디데이", example = "30")
            Integer dDay
    ) {}
    
    @Schema(description = "타임라인 체크리스트 항목 DTO")
    public record ChecklistItemDto(
            @Schema(description = "체크리스트 항목 ID", example = "10")
            Long checklistItemId,

            @Schema(description = "태그 명", example = "FLIGHT")
            String tag,

            @Schema(description = "체크리스트 제목", example = "항공권 예매")
            String title,

            @Schema(description = "디데이 기준", example = "-30")
            int dDay,

            @Schema(description = "체크 완료 여부", example = "false")
            boolean isChecked
    ) {}
}
