package hajiton.chaebee.domain.discovery.repository;

import hajiton.chaebee.domain.discovery.entity.Discovery;
import hajiton.chaebee.domain.discovery.entity.TravelType;
import hajiton.chaebee.domain.trip.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscoveryRepository extends JpaRepository<Discovery, Long> {
    boolean existsByTripId(Long tripId);

    Optional<Discovery> findByTripId(Long tripId);

    @Query("SELECT d FROM Discovery d JOIN FETCH d.trip t JOIN FETCH d.member WHERE d.id = :id")
    Optional<Discovery> findWithTripAndMemberById(@Param("id") Long id);

    /**
     * countryCode, travelType 조건 중 null이면 전체 조회 (동적 필터)
     * Enum 타입으로 직접 받아 타입 불일치 방지
     */
    @Query("""
            SELECT d FROM Discovery d
            JOIN FETCH d.trip t
            JOIN FETCH d.member m
            WHERE (:countryCode IS NULL OR t.countryCode = :countryCode)
              AND (:travelType  IS NULL OR d.travelType  = :travelType)
            """)
    Page<Discovery> findAllByFilter(
            @Param("countryCode") Country countryCode,
            @Param("travelType")  TravelType travelType,
            Pageable pageable
    );
}
