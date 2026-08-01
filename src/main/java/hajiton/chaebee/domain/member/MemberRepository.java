package hajiton.chaebee.domain.member;

import hajiton.chaebee.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 소셜 로그인 제공자(provider)와 고유 ID(providerId)를 조합해 기존 회원 여부를 조회
    Optional<Member> findByProviderAndProviderId(String provider, String providerId);
}