package hajiton.chaebee.domain.trip.repository;

import hajiton.chaebee.domain.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findFirstByMemberIdOrderByDepartureDateDesc(Long memberId);
}
