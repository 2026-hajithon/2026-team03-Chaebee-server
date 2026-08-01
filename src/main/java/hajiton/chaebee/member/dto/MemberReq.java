package hajiton.chaebee.member.dto;

public class MemberReq {

    private MemberReq() {

    }

    public record Login(
            String provider,     // "GOOGLE", "APPLE", "KAKAO", "GUEST" [cite: 5]
            String providerToken
    ) {
    }
}
