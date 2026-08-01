package hajiton.chaebee.domain.member.dto;

import lombok.Builder;

public class MemberRes {

    private MemberRes() {

    }

    @Builder // record에도 Builder를 달아두면 Service에서 값 넣을 때 편해!
    public record Login(
            Long memberId,         // DB에 저장된 회원 PK [cite: 141]
            String name,           // 이름/닉네임 [cite: 141]
            Boolean isGuest,       // 게스트 여부 [cite: 141]
            Boolean isNewMember,   // 이번에 새로 가입한 회원인지 여부 [cite: 141]
            String accessToken,    // 채비(Chaebee) 서버 전용 Access Token
            String refreshToken    // 채비(Chaebee) 서버 전용 Refresh Token
    ) {
    }
}
