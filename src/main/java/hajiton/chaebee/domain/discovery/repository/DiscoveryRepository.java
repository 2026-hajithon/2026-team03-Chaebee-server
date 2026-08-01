package hajiton.chaebee.domain.discovery.repository;

import hajiton.chaebee.domain.discovery.entity.Discovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscoveryRepository extends JpaRepository<Discovery, Long> {
    boolean existsByTripId(Long tripId);

    Optional<Discovery> findByTripId(Long tripId);
}
