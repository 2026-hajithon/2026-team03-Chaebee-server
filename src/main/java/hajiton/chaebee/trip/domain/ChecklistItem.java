package hajiton.chaebee.trip.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip; // 소속 여행

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Tag tag; // 태그

    @Column(nullable = false)
    private Integer dDay; // 기준 D-Day

    @Builder.Default
    @Column(nullable = false)
    private Boolean isChecked = false; // 체크 여부
}
