package hajiton.chaebee.trip.domain;

import hajiton.chaebee.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 여행 생성 유저

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Country countryCode; // 국가 코드

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private City cityCode; // 도시 코드

    private LocalDateTime departureDate; // 출국일

    private LocalDateTime arrivalDate; // 입국일

    private Boolean hasEsim; // 이심 여부

    private Boolean hasCash; // 현금 여부
}
