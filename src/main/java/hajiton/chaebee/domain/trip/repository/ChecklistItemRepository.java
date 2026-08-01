package hajiton.chaebee.domain.trip.repository;

import hajiton.chaebee.domain.trip.entity.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {
    List<ChecklistItem> findByTripIdOrderByDDayDesc(Long tripId);

    List<ChecklistItem> findAllByTripId(Long tripId);
}
