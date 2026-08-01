package hajiton.chaebee.domain.discovery.repository;

import hajiton.chaebee.domain.discovery.entity.DiscoveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscoveryAssignmentRepository extends JpaRepository<DiscoveryAssignment, Long> {
    List<DiscoveryAssignment> findByMemberIdAndAssignedDate(Long memberId, LocalDate assignedDate);
}
