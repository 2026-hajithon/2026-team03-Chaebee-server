package hajiton.chaebee.discovery.domain;

import hajiton.chaebee.member.domain.Member;
import hajiton.chaebee.trip.domain.Trip;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Discovery {

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

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 작성 일시
}
