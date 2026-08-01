package hajiton.chaebee.discovery.repository;

import hajiton.chaebee.discovery.domain.DiscoveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscoveryAssignmentRepository extends JpaRepository<DiscoveryAssignment, Long> {
}
