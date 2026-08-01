package hajiton.chaebee.domain.discovery.entity;

import hajiton.chaebee.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DiscoveryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_discovery_id", nullable = false)
    private SubDiscovery subDiscovery; // 배정된 서브 발견

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 배정받은 회원

    @Column(nullable = false)
    private LocalDate assignedDate; // 배정된 날짜
}
