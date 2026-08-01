package hajiton.chaebee.member.domain;

import jakarta.persistence.*;
import lombok.*;

import java.security.Provider;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name; // 이름/닉네임

    @Column(nullable = false, length = 20)
    private String provider; // 로그인 제공자

    @Column(length = 255)
    private String providerId; // 제공자별 고유 id
}
