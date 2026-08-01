package hajiton.chaebee.domain.discovery.repository;

import hajiton.chaebee.domain.discovery.entity.SubDiscovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubDiscoveryRepository extends JpaRepository<SubDiscovery, Long> {
    List<SubDiscovery> findAllByDiscoveryId(Long id);
}
