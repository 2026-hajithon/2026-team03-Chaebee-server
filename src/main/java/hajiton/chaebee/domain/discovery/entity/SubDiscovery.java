package hajiton.chaebee.domain.discovery.entity;

import hajiton.chaebee.domain.common.BaseCreatedEntity;
import hajiton.chaebee.domain.trip.entity.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SubDiscovery extends BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discovery_id", nullable = false)
    private Discovery discovery; // 소속된 최상위 발견

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Tag tag; // 서브 발견 태그

    @Column(length = 100)
    private String content; // 서브 발견 요약 내용
}