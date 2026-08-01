package hajiton.chaebee.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    // PK_MEMBER 조건 반영
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 이름/닉네임 (프론트에서 전달)
    @Column(name = "이름", length = 50)
    private String name;

    // 로그인 제공자 (GOOGLE, APPLE, GUEST)
    @Column(name = "로그인 제공자", length = 20, nullable = false)
    private String provider;

    // 제공자별 고유 ID (게스트는 NULL 가능)
    @Column(name = "제공자별 고유 id", length = 255)
    private String providerId;
}