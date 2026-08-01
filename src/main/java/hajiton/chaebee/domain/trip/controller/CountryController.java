package hajiton.chaebee.domain.trip.controller;

import hajiton.chaebee.domain.common.dto.ApiResponse;
import hajiton.chaebee.domain.trip.dto.CountryRes;
import hajiton.chaebee.domain.trip.entity.City;
import hajiton.chaebee.domain.trip.entity.Country;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<CountryRes.CountryDto>>> getCountries() {
        List<CountryRes.CountryDto> result = Arrays.stream(Country.values())
                .map(c -> new CountryRes.CountryDto(c.name(), c.getKoreanName(), c.getStatus().name()))
                .toList();
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "도시 목록 조회", description = "특정 국가의 지원하는 도시 목록을 조회합니다.")
    @GetMapping("/{countryCode}/cities")
    public ResponseEntity<ApiResponse<List<CountryRes.CityDto>>> getCities(@PathVariable String countryCode) {
        Country country = Country.valueOf(countryCode);
        List<CountryRes.CityDto> result = Arrays.stream(City.values())
                .filter(c -> c.getCountry() == country)
                .map(c -> new CountryRes.CityDto(c.name(), c.getKoreanName()))
                .toList();
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
