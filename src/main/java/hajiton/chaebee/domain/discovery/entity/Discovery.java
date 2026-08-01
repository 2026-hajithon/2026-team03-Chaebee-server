package hajiton.chaebee.domain.discovery.entity;

import hajiton.chaebee.domain.common.BaseCreatedEntity;
import hajiton.chaebee.domain.member.entity.Member;
import hajiton.chaebee.domain.trip.entity.Trip;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Discovery extends BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false, unique = true)
    private Trip trip; // 연결된 여행 (여행 1개당 발견 1개)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 작성자 회원

    @Enumerated(EnumType.STRING)
    private TravelType travelType; // 여행 유형
}