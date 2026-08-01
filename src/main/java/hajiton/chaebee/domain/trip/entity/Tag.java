package hajiton.chaebee.domain.trip.entity;

public enum Tag {
    PASSPORT("여권 준비", -30),
    VISA("비자·여행허가", -30),
    VACCINATION("예방접종", -30),
    INSURANCE("보험 가입", -14),
    EXCHANGE("환전", -14),
    TRANSIT_CARD("교통카드 준비", -14),
    ADAPTER("어댑터 준비", -7),
    ESIM_ROAMING("로밍·eSIM", -7),
    ENTRY_FORM("입국 신고서", -3),
    FLIGHT_BOARDING("비행기 탑승", -1),
    LOCAL_AIRPORT("현지 공항", 0),
    ACCOMMODATION_CHECKIN("숙소 체크인", 0);

    private final String description;
    private final int dDay;

    Tag(String description, int dDay) {
        this.description = description;
        this.dDay = dDay;
    }

    public String getDescription() { return description; }
    public int getDDay() { return dDay; }
}
