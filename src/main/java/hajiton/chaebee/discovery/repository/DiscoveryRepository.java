package hajiton.chaebee.discovery.repository;

import hajiton.chaebee.discovery.domain.Discovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscoveryRepository extends JpaRepository<Discovery, Long> {
}
