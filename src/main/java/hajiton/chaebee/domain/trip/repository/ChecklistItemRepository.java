package hajiton.chaebee.domain.trip.repository;

import hajiton.chaebee.domain.trip.entity.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {
    @Query("SELECT c FROM ChecklistItem c WHERE c.trip.id = :tripId ORDER BY c.dDay DESC")
    List<ChecklistItem> findByTripIdOrderByDDayDesc(@Param("tripId") Long tripId);
}
