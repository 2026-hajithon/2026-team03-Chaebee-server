package hajiton.chaebee.domain.discovery.repository;

import hajiton.chaebee.domain.discovery.entity.SubDiscovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubDiscoveryRepository extends JpaRepository<SubDiscovery, Long> {
}
