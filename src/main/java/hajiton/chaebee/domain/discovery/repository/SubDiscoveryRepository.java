package hajiton.chaebee.domain.discovery.repository;

import hajiton.chaebee.domain.discovery.entity.SubDiscovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubDiscoveryRepository extends JpaRepository<SubDiscovery, Long> {
    List<SubDiscovery> findAllByDiscoveryId(Long id);

    // 리스트로 넘긴 Discovery ID들에 속하는 모든 SubDiscovery를 한 번에 조회 (IN 쿼리)
    List<SubDiscovery> findAllByDiscoveryIdIn(List<Long> discoveryIds);
}
