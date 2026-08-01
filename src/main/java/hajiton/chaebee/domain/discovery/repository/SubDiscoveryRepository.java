package hajiton.chaebee.domain.discovery.repository;

import hajiton.chaebee.domain.discovery.entity.SubDiscovery;
import hajiton.chaebee.domain.trip.entity.Country;
import hajiton.chaebee.domain.trip.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubDiscoveryRepository extends JpaRepository<SubDiscovery, Long>, JpaSpecificationExecutor<SubDiscovery> {
    List<SubDiscovery> findByDiscoveryId(Long discoveryId);

    @Query("""
            SELECT sd FROM SubDiscovery sd
            JOIN FETCH sd.discovery d
            JOIN FETCH d.trip t
            JOIN FETCH d.member m
            WHERE (:countryCode IS NULL OR t.countryCode = :countryCode)
              AND (:tag IS NULL OR sd.tag = :tag)
            """)
    Page<SubDiscovery> findAllByFilter(
            @Param("countryCode") Country countryCode,
            @Param("tag") Tag tag,
            Pageable pageable
    );

    @Query("""
            SELECT sd FROM SubDiscovery sd
            JOIN FETCH sd.discovery d
            JOIN FETCH d.member m
            WHERE sd.id NOT IN (
                SELECT da.subDiscovery.id FROM DiscoveryAssignment da WHERE da.member.id = :memberId
            )
            AND d.member.id != :memberId
            """)
    List<SubDiscovery> findUnassignedSubDiscoveries(@Param("memberId") Long memberId, Pageable pageable);
}


