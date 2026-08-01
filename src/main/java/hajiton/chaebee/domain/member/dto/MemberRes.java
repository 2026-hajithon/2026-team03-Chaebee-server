package hajiton.chaebee.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class MemberRes {

    private MemberRes() {

    }

    @Schema(description = "로그인 응답 DTO")
    @Builder // record에도 Builder를 달아두면 Service에서 값 넣을 때 편해!
    public record Login(
            @Schema(description = "DB에 저장된 회원 PK", example = "1")
            Long memberId,
            
            @Schema(description = "이름/닉네임", example = "채비유저")
            String name,
            
            @Schema(description = "회원 이메일", example = "chaebee@gmail.com")
            String email,
            
            @Schema(description = "게스트 여부", example = "false")
            Boolean isGuest,
            
            @Schema(description = "신규 가입 여부", example = "true")
            Boolean isNewMember,
            
            @Schema(description = "채비 서버 전용 Access Token")
            String accessToken,
            
            @Schema(description = "채비 서버 전용 Refresh Token")
            String refreshToken
    ) {
    }
}
