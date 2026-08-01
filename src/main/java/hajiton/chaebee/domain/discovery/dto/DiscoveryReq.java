package hajiton.chaebee.domain.discovery.dto;

import hajiton.chaebee.domain.discovery.entity.TravelType;
import hajiton.chaebee.domain.trip.entity.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class DiscoveryReq {

    private DiscoveryReq(){

    }

    @Schema(description = "발견 등록 요청 DTO")
    public record CreateDiscoveryRequest(
            @Schema(description = "여행 ID", example = "1")
            @NotNull Long tripId,
            @Schema(description = "여행 유형", example = "COUPLE")
            @NotNull TravelType tripType,
            @Schema(description = "서브 발견 목록 (1~3개)")
            @Size(min = 1, max = 3, message = "서브 발견은 최소 1개, 최대 3개까지 가능합니다.")
            @Valid
            List<SubDiscoveryRequest> subDiscoveries
    ) {}

    @Schema(description = "서브 발견 요청 DTO")
    public record SubDiscoveryRequest(
            @Schema(description = "태그", example = "FOOD")
            @NotNull Tag tag,
            @Schema(description = "내용", example = "맛집 발견!")
            @NotBlank @Size(max = 100) String content
    ) {}

}
