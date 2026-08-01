package hajiton.chaebee.domain.trip.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public class CountryRes {

    private CountryRes() {}

    @Schema(description = "국가 정보 DTO")
    public record CountryDto(
            @Schema(description = "국가 코드", example = "JAPAN") String countryCode,
            @Schema(description = "한글 국가명", example = "일본") String koreanName,
            @Schema(description = "서비스 상태 (OPEN/COMING_SOON)", example = "OPEN") String status
    ) {}

    @Schema(description = "도시 정보 DTO")
    public record CityDto(
            @Schema(description = "도시 코드", example = "TOKYO") String cityCode,
            @Schema(description = "한글 도시명", example = "도쿄") String koreanName
    ) {}

    @Schema(description = "국가 필수 정보 DTO")
    public record EssentialInfoDto(
            @Schema(description = "국가 코드", example = "JAPAN") String countryCode,
            @Schema(description = "여권 유효기간 규정", example = "입국 시 6개월 이상") String passportValidityRule,
            @Schema(description = "무비자 체류 가능 일수", example = "90") Integer visaFreeStayDays,
            @Schema(description = "공식 사이트 URL", example = "https://...") String officialSiteUrl,
            @Schema(description = "정보 최종 업데이트 일자", example = "2026-07-01") LocalDate lastUpdatedAt
    ) {}
}
