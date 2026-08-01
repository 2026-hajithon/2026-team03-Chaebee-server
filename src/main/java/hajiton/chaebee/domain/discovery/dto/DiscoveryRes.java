package hajiton.chaebee.domain.discovery.dto;

import hajiton.chaebee.domain.discovery.entity.TravelType;
import hajiton.chaebee.domain.trip.entity.Tag;

import java.time.LocalDateTime;
import java.util.List;

public class DiscoveryRes {

    private DiscoveryRes() {

    }

    public record DiscoveryResponse(
            Long discoveryId,
            Long tripId,
            String countryCode,
            String cityCode,
            TravelType travelType,
            LocalDateTime createdAt,
            List<SubDiscoveryResponse> subDiscoveries
    ) {}

    public record SubDiscoveryResponse(
            Long id,
            Tag tag,
            String content
    ) {}
}
