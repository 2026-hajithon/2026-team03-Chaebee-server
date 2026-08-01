package hajiton.chaebee.domain.discovery.controller;

import hajiton.chaebee.domain.discovery.entity.SubDiscovery;
import hajiton.chaebee.domain.discovery.repository.SubDiscoveryRepository;
import hajiton.chaebee.domain.discovery.service.SubDiscoveryService;
import hajiton.chaebee.domain.dto.ApiResponse;
import hajiton.chaebee.domain.trip.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sub-discoveries")
@RequiredArgsConstructor
public class SubDiscoveryController {

    private final SubDiscoveryService subDiscoveryService;

    @GetMapping
    public ApiResponse<?> getSubDiscoveries(
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) String tag) {
        var response = subDiscoveryService.getSubDiscoveries(countryCode, tag);
        return ApiResponse.success(response);
    }

    @GetMapping("/daily")
    public ApiResponse<?> getDailySubDiscoveries(@AuthenticationPrincipal Long memberId) {
        var response = subDiscoveryService.getDailySubDiscoveries(memberId);
        return ApiResponse.success(response);
    }

    // DTO

    public record DailySubDiscoveryResponse(
            LocalDate deliveredDate,
            List<SubDiscoveryDeliveryItem> subDiscoveries
    ) {
        public static DailySubDiscoveryResponse of(LocalDate deliveredDate, List<SubDiscovery> subDiscoveries) {
            List<SubDiscoveryDeliveryItem> items = subDiscoveries.stream()
                    .map(SubDiscoveryDeliveryItem::from)
                    .toList();
            return new DailySubDiscoveryResponse(deliveredDate, items);
        }

        public record SubDiscoveryDeliveryItem(
                Long subDiscoveryId,
                Tag tag,
                String content,
                String authorName
        ) {
            public static SubDiscoveryDeliveryItem from(SubDiscovery subDiscovery) {
                return new SubDiscoveryDeliveryItem(
                        subDiscovery.getId(),
                        subDiscovery.getTag(),
                        subDiscovery.getContent(),
                        subDiscovery.getDiscovery().getMember().getName()
                );
            }
        }
    }
}
