package hajiton.chaebee.discovery.domain;

public enum TravelType {
    SOLO("혼자 여행"),
    FRIEND("우정 여행"),
    COUPLE("커플 여행"),
    FAMILY("가족 여행"),
    GROUP("단체 여행"),
    WITH_PET("반려동물 동반 여행");

    private final String koreanName;

    TravelType(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() { return koreanName; }
}
