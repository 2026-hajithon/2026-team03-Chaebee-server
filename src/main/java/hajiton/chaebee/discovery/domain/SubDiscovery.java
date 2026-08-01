package hajiton.chaebee.discovery.domain;

import hajiton.chaebee.trip.domain.Tag;
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
public class SubDiscovery {

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

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 작성 일시
}
