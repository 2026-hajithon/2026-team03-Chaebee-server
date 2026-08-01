package hajiton.chaebee.domain.discovery.repository;

import hajiton.chaebee.domain.discovery.entity.DiscoveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscoveryAssignmentRepository extends JpaRepository<DiscoveryAssignment, Long> {
    List<DiscoveryAssignment> findByMemberIdAndAssignedDate(Long memberId, LocalDate assignedDate);
    
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM DiscoveryAssignment d WHERE d.subDiscovery.id IN :subDiscoveryIds")
    void deleteBySubDiscoveryIdIn(@org.springframework.data.repository.query.Param("subDiscoveryIds") List<Long> subDiscoveryIds);
}
