package hajiton.chaebee.domain.discovery.repository;

import hajiton.chaebee.domain.discovery.entity.DiscoveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscoveryAssignmentRepository extends JpaRepository<DiscoveryAssignment, Long> {
}
