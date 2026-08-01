package hajiton.chaebee.domain.discovery.service;

import hajiton.chaebee.domain.discovery.controller.DiscoveryController;
import hajiton.chaebee.domain.discovery.entity.Discovery;
import hajiton.chaebee.domain.discovery.entity.SubDiscovery;
import hajiton.chaebee.domain.discovery.entity.TravelType;
import hajiton.chaebee.domain.discovery.repository.DiscoveryRepository;
import hajiton.chaebee.domain.discovery.repository.SubDiscoveryRepository;
import hajiton.chaebee.domain.member.entity.Member;
import hajiton.chaebee.domain.member.repository.MemberRepository;
import hajiton.chaebee.domain.trip.entity.Tag;
import hajiton.chaebee.domain.trip.entity.Country;
import hajiton.chaebee.domain.trip.entity.Trip;
import hajiton.chaebee.domain.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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

    /**
     * 발견 목록 조회 (최신순, 국가/여행유형 필터)
     */
    @Transactional(readOnly = true)
    public DiscoveryListResponse getDiscoveries(String countryCodeStr, String tripTypeStr, int page, int size) {
        // String -> Enum 변환 (null 또는 빈 문자열이면 null → 필터 미적용)
        Country countryCode = (countryCodeStr != null && !countryCodeStr.isBlank())
                ? Country.valueOf(countryCodeStr.toUpperCase()) : null;
        TravelType travelType = (tripTypeStr != null && !tripTypeStr.isBlank())
                ? TravelType.valueOf(tripTypeStr.toUpperCase()) : null;

        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Discovery> discoveryPage = discoveryRepository.findAllByFilter(
                countryCode,
                travelType,
                pageable
        );

        List<DiscoveryListItemResponse> items = discoveryPage.getContent().stream()
                .map(d -> new DiscoveryListItemResponse(
                        d.getId(),
                        d.getTrip().getCountryCode().name(),
                        d.getTrip().getCityCode().name(),
                        d.getTravelType().name(),
                        d.getMember().getName(),
                        d.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new DiscoveryListResponse(
                items,
                discoveryPage.getTotalElements(),
                discoveryPage.getTotalPages(),
                discoveryPage.getNumber()
        );
    }

    /**
     * 발견 상세 조회
     */
    @Transactional(readOnly = true)
    public DiscoveryResponse getDiscovery(Long memberId, Long discoveryId) {
        Discovery discovery = discoveryRepository.findWithTripAndMemberById(discoveryId)
                .orElseThrow(() -> new IllegalArgumentException("발견 정보가 없습니다. (NOT_FOUND)"));

        // 본인 발견 또는 공개 발견이므로 별도 권한 체크 없이 조회 허용
        // (추후 비공개 기능이 생기면 여기서 memberId 검증 추가)

        List<SubDiscovery> subDiscoveries = subDiscoveryRepository.findByDiscoveryId(discoveryId);

        List<SubDiscoveryResponse> subDiscoveryResponses = subDiscoveries.stream()
                .map(sub -> new SubDiscoveryResponse(
                        sub.getId(),
                        sub.getTag().name(),
                        sub.getContent()
                ))
                .collect(Collectors.toList());

        Trip trip = discovery.getTrip();

        return new DiscoveryResponse(
                discovery.getId(),
                trip.getId(),
                trip.getCountryCode().name(),
                trip.getCityCode().name(),
                discovery.getTravelType().name(),
                discovery.getCreatedAt(),
                subDiscoveryResponses
        );
    }

    // 타임라인 카드 한 번에 몇 개까지 보여줄지. 국가+태그 조합마다 팁이 많이 쌓일 수 있어서
    // 전체 목록이 아니라 최신순 상위 N개만 잘라서 카드로 노출.
    private static final int TIMELINE_CARD_LIMIT = 5;


    /**
     * GET /api/sub-discoveries?countryCode=&tag=
     * 타임라인 화면의 시기별(D-14 등) 꿀팁 카드를 채우기 위한 조회.
     * countryCode, tag 둘 다 선택 파라미터 — null이면 해당 조건 없이 전체에서 조회.
     */
    @Transactional(readOnly = true)
    public List<DiscoveryController.SubDiscoveryCardResponse> getSubDiscoveriesForTimeline(String countryCode, Tag tag) {
        Specification<SubDiscovery> spec = Specification
                .where(SubDiscoverySpecs.countryCodeEquals(countryCode))
                .and(SubDiscoverySpecs.tagEquals(tag));

        Pageable pageable = PageRequest.of(0, TIMELINE_CARD_LIMIT, Sort.by(Sort.Direction.DESC, "createdAt"));

        return subDiscoveryRepository.findAll(spec, pageable)
                .map(DiscoveryController.SubDiscoveryCardResponse::from)
                .getContent();
    }


//import org.springframework.data.jpa.domain.Specification;

    // GET /api/sub-discoveries?countryCode=&tag= 의 countryCode, tag는 둘 다 선택값이라
// null이면 조건에서 빠지도록 Specification으로 분리함.
    public class SubDiscoverySpecs {

        private SubDiscoverySpecs() {}

        public static Specification<SubDiscovery> countryCodeEquals(String countryCode) {
            return (root, query, cb) -> countryCode == null
                    ? null
                    : cb.equal(root.get("discovery").get("trip").get("countryCode"), countryCode);
        }

        public static Specification<SubDiscovery> tagEquals(Tag tag) {
            return (root, query, cb) -> tag == null
                    ? null
                    : cb.equal(root.get("tag"), tag);
        }


    }

    // 명세서 기반 DTO
    public record SubDiscoveryRequest(String tag, String content) {}

    public record DiscoveryListItemResponse(
            Long discoveryId,
            String countryCode,
            String cityCode,
            String tripType,
            String authorName,
            java.time.LocalDateTime createdAt
    ) {}

    public record DiscoveryListResponse(
            List<DiscoveryListItemResponse> content,
            long totalElements,
            int totalPages,
            int currentPage
    ) {}

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
