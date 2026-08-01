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

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM Discovery d WHERE d.trip.id = :tripId")
    void deleteByTripId(@Param("tripId") Long tripId);

    @Query("SELECT d FROM Discovery d JOIN FETCH d.trip t JOIN FETCH d.member WHERE d.id = :id")
    Optional<Discovery> findWithTripAndMemberById(@Param("id") Long id);

    /**
     * 전체 조회 (최신순 등 페이징) - N+1 방지용 페치 조인
     */
    @Query(value = "SELECT d FROM Discovery d JOIN FETCH d.trip t JOIN FETCH d.member m",
           countQuery = "SELECT count(d) FROM Discovery d")
    Page<Discovery> findAllWithTripAndMember(Pageable pageable);

    @Query("SELECT d FROM Discovery d JOIN FETCH d.trip t JOIN FETCH d.member m WHERE d.member.id = :memberId ORDER BY d.createdAt DESC")
    java.util.List<Discovery> findAllByMemberIdOrderByCreatedAtDesc(@Param("memberId") Long memberId);
}
