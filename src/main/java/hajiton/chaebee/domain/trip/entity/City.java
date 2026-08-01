package hajiton.chaebee.domain.trip.entity;

import java.util.List;
import java.util.stream.Collectors;

public enum City {

    // ── USA ──
    LOS_ANGELES(
        "로스앤젤레스", Country.USA,
        List.of(
            Tag.PASSPORT,
            Tag.INSURANCE,
            Tag.VISA,
            Tag.ESIM_ROAMING,
            Tag.EXCHANGE,
            Tag.ENTRY_FORM
        )
    ),
    NEW_YORK(
        "뉴욕", Country.USA,
        List.of(
            Tag.PASSPORT,
            Tag.INSURANCE,
            Tag.VISA,
            Tag.TRANSIT_CARD,
            Tag.ESIM_ROAMING,
            Tag.EXCHANGE,
            Tag.ENTRY_FORM
        )
    ),
    HONOLULU(
        "호놀룰루", Country.USA,
        List.of(
            Tag.PASSPORT,
            Tag.INSURANCE,
            Tag.VISA,
            Tag.ESIM_ROAMING,
            Tag.EXCHANGE,
            Tag.ENTRY_FORM
        )
    ),
    LAS_VEGAS(
        "라스베이거스", Country.USA,
        List.of(
            Tag.PASSPORT,
            Tag.INSURANCE,
            Tag.VISA,
            Tag.ESIM_ROAMING,
            Tag.EXCHANGE,
            Tag.ENTRY_FORM
        )
    ),
    SAN_FRANCISCO(
        "샌프란시스코", Country.USA,
        List.of(
            Tag.PASSPORT,
            Tag.INSURANCE,
            Tag.VISA,
            Tag.TRANSIT_CARD,
            Tag.ESIM_ROAMING,
            Tag.EXCHANGE,
            Tag.ENTRY_FORM
        )
    ),

    // ── TAIWAN ──
    TAIPEI(
        "타이베이", Country.TAIWAN,
        List.of(
            Tag.PASSPORT,
            Tag.INSURANCE,
            Tag.TRANSIT_CARD,
            Tag.ESIM_ROAMING,
            Tag.EXCHANGE,
            Tag.ENTRY_FORM
        )
    ),

    // ── SINGAPORE ──
    SINGAPORE_CITY(
        "싱가포르", Country.SINGAPORE,
        List.of(
            Tag.PASSPORT,
            Tag.INSURANCE,
            Tag.TRANSIT_CARD,
            Tag.ESIM_ROAMING,
            Tag.EXCHANGE,
            Tag.ENTRY_FORM
        )
    );

    private final String koreanName;
    private final Country country;
    private final List<Tag> applicableTags;

    City(String koreanName, Country country, List<Tag> applicableTags) {
        this.koreanName = koreanName;
        this.country = country;
        this.applicableTags = applicableTags;
    }

    public String getKoreanName() { return koreanName; }
    public Country getCountry() { return country; }

    public List<Tag> getTagsByDday(int dDay) {
        return applicableTags.stream()
                .filter(tag -> tag.getDDay() == -dDay)
                .collect(Collectors.toList());
    }
}
