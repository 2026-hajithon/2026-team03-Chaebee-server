package hajiton.chaebee.domain.member;

import hajiton.chaebee.domain.member.dto.MemberReq;
import hajiton.chaebee.domain.member.dto.MemberRes;
import hajiton.chaebee.domain.member.Member;
import hajiton.chaebee.domain.member.MemberRepository;
import hajiton.chaebee.security.GoogleTokenVerifier;
import hajiton.chaebee.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // 👈 롬복 로거 임포트
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j // 👈 로거 활성화
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final GoogleTokenVerifier googleTokenVerifier;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public MemberRes.Login login(MemberReq.Login request) {
        log.info("[로그인 요청] provider: {}, providerToken 존재 여부: {}",
                request.provider(), request.providerToken() != null);

        String providerId;
        String name = "채비유저";
        boolean isGuest = "GUEST".equals(request.provider());

        // 1. Provider에 따른 토큰 검증 분기 처리
        if ("GOOGLE".equals(request.provider())) {
            providerId = googleTokenVerifier.verify(request.providerToken());
            log.info("[구글 토큰 검증 성공] providerId: {}", providerId);
        } else if (isGuest) {
            providerId = null;
            log.info("[게스트 로그인 처리]");
        } else {
            log.error("[지원하지 않는 로그인 방식] provider: {}", request.provider());
            throw new IllegalArgumentException("아직 지원하지 않는 로그인 방식입니다: " + request.provider());
        }

        // 2. 신규 가입 여부 플래그 및 기존 회원 조회
        boolean isNewMember = false;
        Member member = null;

        if (!isGuest) {
            member = memberRepository.findByProviderAndProviderId(request.provider(), providerId)
                    .orElse(null);

            if (member == null) {
                log.info("[구글 신규 회원가입 진행] provider: {}, providerId: {}", request.provider(), providerId);
                member = Member.builder()
                        .name(name)
                        .provider(request.provider())
                        .providerId(providerId)
                        .build();
                member = memberRepository.save(member);
                isNewMember = true;
            } else {
                log.info("[구글 기존 회원 로그인] memberId: {}", member.getId());
            }
        } else {
            log.info("[게스트 신규 회원 생성]");
            member = Member.builder()
                    .name("게스트")
                    .provider("GUEST")
                    .build();
            member = memberRepository.save(member);
            isNewMember = true;
        }

        // 3. JwtTokenProvider를 사용한 실제 토큰 발급
        Long memberId = member.getId();
        String accessToken = jwtTokenProvider.createAccessToken(memberId);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId);
        log.info("[토큰 발급 완료] memberId: {}", memberId);

        // 4. MemberRes.Login 레코드 스펙에 맞춰서 반환
        return MemberRes.Login.builder()
                .memberId(memberId)
                .name(member.getName())
                .isGuest(isGuest)
                .isNewMember(isNewMember)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}