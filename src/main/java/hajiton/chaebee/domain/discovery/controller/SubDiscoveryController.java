package hajiton.chaebee.domain.discovery.controller;

import hajiton.chaebee.domain.discovery.entity.SubDiscovery;
import hajiton.chaebee.domain.discovery.repository.SubDiscoveryRepository;
import hajiton.chaebee.domain.discovery.service.SubDiscoveryService;
import hajiton.chaebee.domain.dto.ApiResponse;
import hajiton.chaebee.domain.trip.entity.Tag;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ApiResponse<?> getSubDiscoveries(
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) String tag) {
        var response = subDiscoveryService.getSubDiscoveries(countryCode, tag);
        return ApiResponse.success(response);
    }

    @Operation(summary = "일일 서브 발견 조회", description = "매일 새롭게 배정되는 서브 발견을 조회합니다.")
    @GetMapping("/daily")
    public ApiResponse<?> getDailySubDiscoveries(@AuthenticationPrincipal Long memberId) {
        var response = subDiscoveryService.getDailySubDiscoveries(memberId);
        return ApiResponse.success(response);
    }

    // DTO

    @Schema(description = "일일 서브 발견 응답 DTO")
    public record DailySubDiscoveryResponse(
            @Schema(description = "배정된 날짜", example = "2026-07-31") LocalDate deliveredDate,
            @Schema(description = "서브 발견 목록") List<SubDiscoveryDeliveryItem> subDiscoveries
    ) {
        public static DailySubDiscoveryResponse of(LocalDate deliveredDate, List<SubDiscovery> subDiscoveries) {
            List<SubDiscoveryDeliveryItem> items = subDiscoveries.stream()
                    .map(SubDiscoveryDeliveryItem::from)
                    .toList();
            return new DailySubDiscoveryResponse(deliveredDate, items);
        }

        @Schema(description = "서브 발견 배정 아이템 DTO")
        public record SubDiscoveryDeliveryItem(
                @Schema(description = "서브 발견 ID", example = "1") Long subDiscoveryId,
                @Schema(description = "태그", example = "FOOD") Tag tag,
                @Schema(description = "내용", example = "맛집 발견!") String content,
                @Schema(description = "작성자 이름", example = "홍길동") String authorName
        ) {
            public static SubDiscoveryDeliveryItem from(SubDiscovery subDiscovery) {
                return new SubDiscoveryDeliveryItem(
                        subDiscovery.getId(),
                        subDiscovery.getTag(),
                        subDiscovery.getContent(),
                        subDiscovery.getDiscovery().getMember().getName()
                );
            }
        }
    }
}
