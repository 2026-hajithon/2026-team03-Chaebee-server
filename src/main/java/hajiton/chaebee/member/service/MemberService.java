package hajiton.chaebee.member.service;

import hajiton.chaebee.member.domain.LoginProvider;
import hajiton.chaebee.member.domain.Member;
import hajiton.chaebee.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;

    /**
     * 소셜 로그인 및 게스트 로그인 처리
     */
    @Transactional
    public LoginResponse login(LoginProvider provider, String providerToken) {
        boolean isGuest = (provider == LoginProvider.GUEST);
        String providerId = null;
        String name = "게스트";

        if (!isGuest) {
            // TODO: (팀원 구현 부분) 구글/애플 서버와 통신하여 providerToken 검증 후 유저 고유 ID와 이름 추출
            // 여기서는 팀원분이 붙이실 로직을 위해 임시로 토큰을 ID로 씁니다.
            providerId = "extracted_id_from_token";
            name = "소셜유저"; 
        }

        final String finalProviderId = providerId;
        final String finalName = name;
        
        Member member;
        boolean isNewMember = false;

        if (isGuest) {
            // 게스트는 매번 새 회원으로 취급한다고 임시 가정 (단말기 ID 등 활용할 경우 로직 변경 필요)
            member = Member.builder()
                    .loginProvider(LoginProvider.GUEST)
                    .name(finalName)
                    .build();
            memberRepository.save(member);
            isNewMember = true;
        } else {
            // 기존 가입된 소셜 유저인지 확인
            Member existingMember = memberRepository.findByProviderIdAndLoginProvider(providerId, provider)
                    .orElse(null);
            
            if (existingMember == null) {
                // 신규 가입
                isNewMember = true;
                member = Member.builder()
                        .loginProvider(provider)
                        .providerId(finalProviderId)
                        .name(finalName)
                        .build();
                memberRepository.save(member);
            } else {
                // 기존 유저 로그인
                member = existingMember;
            }
        }

        // TODO: (팀원 구현 부분) JWT 토큰 발급 로직 연동
        String accessToken = "dummy_access_token_for_" + member.getId();
        String refreshToken = "dummy_refresh_token_for_" + member.getId();

        return new LoginResponse(
                member.getId(),
                member.getName(),
                isGuest,
                isNewMember,
                accessToken,
                refreshToken
        );
    }
    
    @Transactional(readOnly = true)
    public MemberInfoResponse getMe(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        
        return new MemberInfoResponse(member.getId(), member.getName(), member.getLoginProvider());
    }

    // 명세서 규격에 맞춘 응답 DTO
    public record LoginResponse(
            Long memberId,
            String name,
            boolean isGuest,
            boolean isNewMember,
            String accessToken,
            String refreshToken
    ) {}
    
    public record MemberInfoResponse(
            Long memberId,
            String name,
            LoginProvider provider
    ) {}
}
