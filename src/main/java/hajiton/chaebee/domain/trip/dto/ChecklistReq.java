package hajiton.chaebee.domain.trip.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ChecklistReq {

    private ChecklistReq() {}

    @Schema(description = "체크리스트 업데이트 요청 DTO")
    public record UpdateChecklistRequest(
            @Schema(description = "체크 여부", example = "true")
            Boolean isChecked
    ) {}
}
