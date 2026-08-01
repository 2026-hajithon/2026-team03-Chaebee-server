package hajiton.chaebee.discovery.repository;

import hajiton.chaebee.discovery.domain.SubDiscovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubDiscoveryRepository extends JpaRepository<SubDiscovery, Long> {
}
