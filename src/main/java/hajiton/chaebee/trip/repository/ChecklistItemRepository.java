package hajiton.chaebee.trip.repository;

import hajiton.chaebee.trip.domain.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {
    List<ChecklistItem> findByTripIdOrderByDDayDesc(Long tripId);
}
