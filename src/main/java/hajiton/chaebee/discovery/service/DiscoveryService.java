package hajiton.chaebee.discovery.service;

import hajiton.chaebee.discovery.domain.Discovery;
import hajiton.chaebee.discovery.domain.SubDiscovery;
import hajiton.chaebee.discovery.domain.TravelType;
import hajiton.chaebee.discovery.repository.DiscoveryRepository;
import hajiton.chaebee.discovery.repository.SubDiscoveryRepository;
import hajiton.chaebee.member.domain.Member;
import hajiton.chaebee.member.repository.MemberRepository;
import hajiton.chaebee.trip.domain.Tag;
import hajiton.chaebee.trip.domain.Trip;
import hajiton.chaebee.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscoveryService {

    private final DiscoveryRepository discoveryRepository;
    private final SubDiscoveryRepository subDiscoveryRepository;
    private final TripRepository tripRepository;
    private final MemberRepository memberRepository;

    /**
     * 발견 등록 (여행 1개당 1회)
     */
    @Transactional
    public DiscoveryResponse createDiscovery(Long memberId, Long tripId, String tripTypeStr, List<SubDiscoveryRequest> subDiscoveriesReq) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("여행 정보가 없습니다. (TRIP_NOT_FOUND)"));

        if (!trip.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("본인의 여행에만 발견을 등록할 수 있습니다. (FORBIDDEN)");
        }

        if (discoveryRepository.existsByTripId(tripId)) {
            throw new IllegalStateException("이미 해당 여행에 등록된 발견이 있습니다. (DUPLICATED_DISCOVERY)");
        }

        TravelType travelType = TravelType.valueOf(tripTypeStr);

        Discovery discovery = Discovery.builder()
                .trip(trip)
                .member(member)
                .travelType(travelType)
                .build();

        Discovery savedDiscovery = discoveryRepository.save(discovery);

        List<SubDiscoveryResponse> subDiscoveryResponses = subDiscoveriesReq.stream().map(req -> {
            Tag tag = Tag.valueOf(req.tag());
            SubDiscovery sub = SubDiscovery.builder()
                    .discovery(savedDiscovery)
                    .tag(tag)
                    .content(req.content())
                    .build();
            subDiscoveryRepository.save(sub);
            
            return new SubDiscoveryResponse(sub.getId(), tag.name(), sub.getContent());
        }).collect(Collectors.toList());

        return new DiscoveryResponse(
                savedDiscovery.getId(),
                trip.getId(),
                trip.getCountryCode().name(),
                trip.getCityCode().name(),
                savedDiscovery.getTravelType().name(),
                savedDiscovery.getCreatedAt(),
                subDiscoveryResponses
        );
    }

    // 명세서 기반 DTO
    public record SubDiscoveryRequest(String tag, String content) {}

    public record DiscoveryResponse(
            Long discoveryId,
            Long tripId,
            String countryCode,
            String cityCode,
            String tripType,
            java.time.LocalDateTime createdAt,
            List<SubDiscoveryResponse> subDiscoveries
    ) {}

    public record SubDiscoveryResponse(
            Long subDiscoveryId,
            String tag,
            String content
    ) {}
}
