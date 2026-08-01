package hajiton.chaebee.trip.domain;

public enum Country {
    USA("미국", "USA", "USD", Status.OPEN),
    TAIWAN("대만", "TWN", "TWD", Status.OPEN),
    SINGAPORE("싱가포르", "SGP", "SGD", Status.OPEN),
    JAPAN("일본", "JPN", "JPY", Status.COMING_SOON),
    BRAZIL("브라질", "BRA", "BRL", Status.COMING_SOON),
    AUSTRALIA("호주", "AUS", "AUD", Status.COMING_SOON),
    THAILAND("태국", "THA", "THB", Status.COMING_SOON),
    VIETNAM("베트남", "VNM", "VND", Status.COMING_SOON),
    HONGKONG("홍콩", "HKG", "HKD", Status.COMING_SOON),
    FRANCE("프랑스", "FRA", "EUR", Status.COMING_SOON);

    public enum Status {
        OPEN, COMING_SOON
    }

    private final String koreanName;
    private final String isoCode;
    private final String currency;
    private final Status status;

    Country(String koreanName, String isoCode, String currency, Status status) {
        this.koreanName = koreanName;
        this.isoCode = isoCode;
        this.currency = currency;
        this.status = status;
    }

    public String getKoreanName() { return koreanName; }
    public String getIsoCode() { return isoCode; }
    public String getCurrency() { return currency; }
    public Status getStatus() { return status; }
}
