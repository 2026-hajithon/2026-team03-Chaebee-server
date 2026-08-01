package hajiton.chaebee.domain.discovery.dto;

import hajiton.chaebee.domain.discovery.entity.TravelType;
import hajiton.chaebee.domain.trip.entity.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class DiscoveryReq {

    private DiscoveryReq(){

    }

    public record CreateDiscoveryRequest(
            @NotNull Long tripId,
            String countryCode,
            String cityCode,
            @NotNull TravelType travelType,
            @Size(min = 1, max = 3, message = "서브 발견은 최소 1개, 최대 3개까지 가능합니다.")
            @Valid
            List<SubDiscoveryRequest> subDiscoveries
    ) {}

    public record SubDiscoveryRequest(
            @NotNull Tag tag,
            @NotBlank @Size(max = 100) String content
    ) {}

}
