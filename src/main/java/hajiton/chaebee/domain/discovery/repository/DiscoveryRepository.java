package hajiton.chaebee.domain.discovery.repository;

import hajiton.chaebee.domain.discovery.entity.Discovery;
import hajiton.chaebee.domain.trip.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscoveryRepository extends JpaRepository<Discovery, Long> {
    boolean existsByTripId(Long tripId);

    Optional<Discovery> findByTripId(Long tripId);

    // 연관된 Trip의 cityCode가 일치하는 모든 Discovery 조회
    List<Discovery> findAllByTrip_CityCode(City cityCode);
}
