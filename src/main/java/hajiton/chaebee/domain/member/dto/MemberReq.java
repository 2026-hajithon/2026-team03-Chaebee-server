package hajiton.chaebee.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class MemberReq {

    private MemberReq() {

    }

    @Schema(description = "로그인 요청 DTO")
    public record Login(
            @Schema(description = "로그인 제공자 (GOOGLE, APPLE, KAKAO, GUEST)", example = "GOOGLE")
            String provider,
            
            @Schema(description = "소셜 제공자로부터 받은 토큰 (GUEST일 경우 null/빈 문자열 가능)", example = "abc123token")
            String providerToken
    ) {
    }
}
