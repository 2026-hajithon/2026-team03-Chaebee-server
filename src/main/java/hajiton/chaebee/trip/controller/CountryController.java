package hajiton.chaebee.trip.controller;

import hajiton.chaebee.common.dto.ApiResponse;
import hajiton.chaebee.trip.domain.City;
import hajiton.chaebee.trip.domain.Country;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    @GetMapping
    public ApiResponse<?> getCountries() {
        List<CountryDto> result = Arrays.stream(Country.values())
                .map(c -> new CountryDto(c.name(), c.getKoreanName(), c.getStatus().name()))
                .toList();
        return ApiResponse.success(result);
    }

    @GetMapping("/{countryCode}/cities")
    public ApiResponse<?> getCities(@PathVariable String countryCode) {
        Country country = Country.valueOf(countryCode);
        List<CityDto> result = Arrays.stream(City.values())
                .filter(c -> c.getCountry() == country)
                .map(c -> new CityDto(c.name(), c.getKoreanName()))
                .toList();
        return ApiResponse.success(result);
    }

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

    public record CountryDto(String countryCode, String koreanName, String status) {}
    public record CityDto(String cityCode, String koreanName) {}
    public record EssentialInfoDto(
            String countryCode,
            String passportValidityRule,
            Integer visaFreeStayDays,
            String officialSiteUrl,
            LocalDate lastUpdatedAt
    ) {}
}
