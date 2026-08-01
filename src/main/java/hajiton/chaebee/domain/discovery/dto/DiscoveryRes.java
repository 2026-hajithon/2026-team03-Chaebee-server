package hajiton.chaebee.domain.discovery.dto;

import hajiton.chaebee.domain.discovery.entity.TravelType;
import hajiton.chaebee.domain.trip.entity.Tag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.media.Schema;
import hajiton.chaebee.domain.discovery.entity.SubDiscovery;

public class DiscoveryRes {

    private DiscoveryRes() {

    }

    @Schema(description = "발견 상세 응답 DTO")
    public record DiscoveryResponse(
            @Schema(description = "발견 ID", example = "1") Long discoveryId,
            @Schema(description = "여행 ID", example = "1") Long tripId,
            @Schema(description = "국가 코드", example = "JAPAN") String countryCode,
            @Schema(description = "도시 코드", example = "TOKYO") String cityCode,
            @Schema(description = "여행 유형", example = "COUPLE") TravelType tripType,
            @Schema(description = "생성 일시") LocalDateTime createdAt,
            @Schema(description = "서브 발견 목록") List<SubDiscoveryResponse> subDiscoveries
    ) {}

    @Schema(description = "서브 발견 응답 DTO")
    public record SubDiscoveryResponse(
            @Schema(description = "서브 발견 ID", example = "1") Long subDiscoveryId,
            @Schema(description = "태그", example = "FOOD") Tag tag,
            @Schema(description = "내용", example = "맛집 발견!") String content
    ) {}

    @Schema(description = "서브 발견 타임라인 카드 DTO")
    public record SubDiscoveryCardResponse(
            @Schema(description = "서브 발견 ID", example = "1") Long subDiscoveryId,
            @Schema(description = "태그", example = "FOOD") Tag tag,
            @Schema(description = "내용", example = "맛집 발견!") String content
    ) {
        public static SubDiscoveryCardResponse from(SubDiscovery subDiscovery) {
            return new SubDiscoveryCardResponse(
                    subDiscovery.getId(),
                    subDiscovery.getTag(),
                    subDiscovery.getContent()
            );
        }
    }

    @Schema(description = "발견 목록 응답 DTO")
    public record DiscoveryListResponse(
            @Schema(description = "발견 목록") List<DiscoveryListItemResponse> content,
            @Schema(description = "전체 요소 수", example = "100") long totalElements,
            @Schema(description = "전체 페이지 수", example = "10") int totalPages,
            @Schema(description = "현재 페이지 (0부터 시작)", example = "0") int currentPage
    ) {}

    @Schema(description = "발견 목록 아이템 DTO")
    public record DiscoveryListItemResponse(
            @Schema(description = "발견 ID", example = "1") Long discoveryId,
            @Schema(description = "국가 코드", example = "JAPAN") String countryCode,
            @Schema(description = "도시 코드", example = "TOKYO") String cityCode,
            @Schema(description = "여행 유형", example = "COUPLE") String tripType,
            @Schema(description = "작성자 이름", example = "홍길동") String authorName,
            @Schema(description = "생성 일시") LocalDateTime createdAt
    ) {}


}
