package hajiton.chaebee.domain.trip.controller;

import hajiton.chaebee.domain.dto.ApiResponse;
import hajiton.chaebee.domain.trip.entity.City;
import hajiton.chaebee.domain.trip.entity.Country;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "Country API", description = "국가 및 도시 정보 관련 API")
@RestController
@RequestMapping("/api/countries")
public class CountryController {

    @Operation(summary = "국가 목록 조회", description = "지원하는 전체 국가 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<?> getCountries() {
        List<CountryDto> result = Arrays.stream(Country.values())
                .map(c -> new CountryDto(c.name(), c.getKoreanName(), c.getStatus().name()))
                .toList();
        return ApiResponse.success(result);
    }

    @Operation(summary = "도시 목록 조회", description = "특정 국가의 지원하는 도시 목록을 조회합니다.")
    @GetMapping("/{countryCode}/cities")
    public ApiResponse<?> getCities(@PathVariable String countryCode) {
        Country country = Country.valueOf(countryCode);
        List<CityDto> result = Arrays.stream(City.values())
                .filter(c -> c.getCountry() == country)
                .map(c -> new CityDto(c.name(), c.getKoreanName()))
                .toList();
        return ApiResponse.success(result);
    }

    @Operation(summary = "필수 정보 조회", description = "특정 국가의 필수 정보(여권, 비자 등)를 조회합니다.")
    @GetMapping("/{countryCode}/essential-info")
    public ApiResponse<?> getEssentialInfo(@PathVariable String countryCode) {
        Country country = Country.valueOf(countryCode);
        EssentialInfoDto info = new EssentialInfoDto(
                country.name(),
                country.getPassportValidityRule(),
                country.getVisaFreeStayDays(),
                country.getOfficialSiteUrl(),
                country.getLastUpdatedAt()
        );
        return ApiResponse.success(info);
    }

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
