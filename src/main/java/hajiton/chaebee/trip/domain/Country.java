package hajiton.chaebee.trip.domain;

import lombok.Getter;
import java.time.LocalDate;

@Getter
public enum Country {
    USA("미국", "USA", "USD", Status.OPEN, "입국일 기준 최소 6개월 이상 유효", 90, "https://esta.cbp.dhs.gov", LocalDate.of(2026, 6, 1)),
    TAIWAN("대만", "TWN", "TWD", Status.OPEN, "입국일 기준 최소 6개월 이상 유효", 90, "https://nia.gov.tw", LocalDate.of(2026, 6, 1)),
    SINGAPORE("싱가포르", "SGP", "SGD", Status.OPEN, "입국일 기준 최소 6개월 이상 유효", 90, "https://ica.gov.sg", LocalDate.of(2026, 6, 1)),
    JAPAN("일본", "JPN", "JPY", Status.COMING_SOON, null, null, null, null),
    BRAZIL("브라질", "BRA", "BRL", Status.COMING_SOON, null, null, null, null),
    AUSTRALIA("호주", "AUS", "AUD", Status.COMING_SOON, null, null, null, null),
    THAILAND("태국", "THA", "THB", Status.COMING_SOON, null, null, null, null),
    VIETNAM("베트남", "VNM", "VND", Status.COMING_SOON, null, null, null, null),
    HONGKONG("홍콩", "HKG", "HKD", Status.COMING_SOON, null, null, null, null),
    FRANCE("프랑스", "FRA", "EUR", Status.COMING_SOON, null, null, null, null);

    public enum Status {
        OPEN, COMING_SOON
    }

    private final String koreanName;
    private final String isoCode;
    private final String currency;
    private final Status status;
    private final String passportValidityRule;
    private final Integer visaFreeStayDays;
    private final String officialSiteUrl;
    private final LocalDate lastUpdatedAt;

    Country(String koreanName, String isoCode, String currency, Status status,
            String passportValidityRule, Integer visaFreeStayDays, String officialSiteUrl, LocalDate lastUpdatedAt) {
        this.koreanName = koreanName;
        this.isoCode = isoCode;
        this.currency = currency;
        this.status = status;
        this.passportValidityRule = passportValidityRule;
        this.visaFreeStayDays = visaFreeStayDays;
        this.officialSiteUrl = officialSiteUrl;
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
