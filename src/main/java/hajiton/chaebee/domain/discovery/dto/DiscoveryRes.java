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


    // ==========================================
    @Schema(description = "타임라인 응답 DTO")
    public record TimelineResponse(
            @Schema(description = "상단 여행 요약 정보") TripInfo tripInfo,
            @Schema(description = "D-Day별 타임라인 데이터 목록") List<TimelineGroup> timeline,
            @Schema(description = "하단 국가 필수 정보") EssentialInfo essentialInfo
    ) {}

    @Schema(description = "타임라인 상단 여행 정보 DTO")
    public record TripInfo(
            @Schema(description = "도착지 정보 (도시, 국가)", example = "도쿄, 일본") String destination,
            @Schema(description = "D-Day", example = "-14") long dDay,
            @Schema(description = "체크리스트 진척도 정보") Progress progress
    ) {}

    @Schema(description = "체크리스트 진척도 DTO")
    public record Progress(
            @Schema(description = "전체 항목 수", example = "10") int total,
            @Schema(description = "완료된 항목 수", example = "3") int completed,
            @Schema(description = "완료율 (%)", example = "30") int percentage
    ) {}

    @Schema(description = "D-Day별 타임라인 그룹 DTO")
    public record TimelineGroup(
            @Schema(description = "D-Day", example = "-14") int dDay,
            @Schema(description = "해당 날짜", example = "2026-08-26") LocalDate date,
            @Schema(description = "해당 D-Day의 팁/발견 목록") List<TimelineDiscovery> discoveries,
            @Schema(description = "해당 D-Day의 체크리스트 목록") List<TimelineChecklist> checklists
    ) {}

    @Schema(description = "타임라인 내 발견 항목 DTO")
    public record TimelineDiscovery(
            @Schema(description = "태그", example = "PASSPORT") Tag tag,
            @Schema(description = "제목", example = "여권 재발급") String title,
            @Schema(description = "내용", example = "여권 만료일이 6개월 이상 남았는지 확인하세요.") String content
    ) {}

    @Schema(description = "타임라인 내 체크리스트 항목 DTO")
    public record TimelineChecklist(
            @Schema(description = "체크리스트 ID", example = "1") Long checklistId,
            @Schema(description = "태그", example = "FLIGHT") Tag tag,
            @Schema(description = "제목", example = "항공권 예매") String title,
            @Schema(description = "체크 여부", example = "false") boolean isChecked
    ) {}

    // Country Enum에서 가져올 하단 필수 정보 영역
    @Schema(description = "국가 필수 정보 DTO")
    public record EssentialInfo(
            @Schema(description = "여권 유효기간 규정", example = "입국 시 6개월 이상") String passportValidityRule,
            @Schema(description = "무비자 체류 가능 일수", example = "90") Integer visaFreeStayDays,
            @Schema(description = "공식 사이트 URL", example = "https://...") String officialSiteUrl,
            @Schema(description = "정보 최종 업데이트 일자", example = "2026-07-01") LocalDate lastUpdatedAt
    ) {}
}
