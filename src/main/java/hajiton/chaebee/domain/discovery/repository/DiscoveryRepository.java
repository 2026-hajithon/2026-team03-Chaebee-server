package hajiton.chaebee.domain.discovery.repository;

import hajiton.chaebee.domain.discovery.entity.Discovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscoveryRepository extends JpaRepository<Discovery, Long> {
    boolean existsByTripId(Long tripId);
}
