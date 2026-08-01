package hajiton.chaebee.domain.discovery.service;

import hajiton.chaebee.domain.discovery.controller.DiscoveryController;
import hajiton.chaebee.domain.discovery.dto.DiscoveryReq;
import hajiton.chaebee.domain.discovery.dto.DiscoveryRes;
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
import hajiton.chaebee.domain.trip.entity.*;
import hajiton.chaebee.domain.trip.repository.ChecklistItemRepository;
import hajiton.chaebee.domain.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j // 💡 로깅을 위한 어노테이션 추가
@Service
@RequiredArgsConstructor
public class DiscoveryService {

    private final DiscoveryRepository discoveryRepository;
    private final SubDiscoveryRepository subDiscoveryRepository;
    private final TripRepository tripRepository;
    private final MemberRepository memberRepository;
    private final ChecklistItemRepository checklistItemRepository;

    // 발견 등록
    @Transactional
    public DiscoveryRes.DiscoveryResponse createDiscovery(Long memberId, DiscoveryReq.CreateDiscoveryRequest request) {
        log.info("발견 등록 요청 시작 - memberId: {}, tripId: {}", memberId, request.tripId());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("발견 등록 실패 - 존재하지 않는 회원 (memberId: {})", memberId);
                    return new IllegalArgumentException("회원 정보가 없습니다.");
                });

        Trip trip = tripRepository.findById(request.tripId())
                .orElseThrow(() -> {
                    log.warn("발견 등록 실패 - 존재하지 않는 여행 (tripId: {})", request.tripId());
                    return new IllegalArgumentException("여행 정보가 없습니다. (TRIP_NOT_FOUND)");
                });

        if (!trip.getMember().getId().equals(memberId)) {
            log.warn("발견 등록 권한 없음 - memberId: {}가 tripId: {}에 접근 시도", memberId, request.tripId());
            throw new IllegalArgumentException("본인의 여행에만 발견을 등록할 수 있습니다. (FORBIDDEN)");
        }

        if (discoveryRepository.existsByTripId(request.tripId())) {
            log.warn("발견 등록 실패 - 이미 등록된 발견 존재 (tripId: {})", request.tripId());
            throw new IllegalStateException("이미 해당 여행에 등록된 발견이 있습니다. (DUPLICATED_DISCOVERY)");
        }

        Discovery discovery = Discovery.builder()
                .trip(trip)
                .member(member)
                .travelType(request.tripType())
                .build();

        Discovery savedDiscovery = discoveryRepository.save(discovery);
        log.debug("부모 Discovery 저장 완료 (discoveryId: {})", savedDiscovery.getId());

        List<DiscoveryRes.SubDiscoveryResponse> subDiscoveryResponses = request.subDiscoveries().stream()
                .map(req -> {
                    SubDiscovery sub = SubDiscovery.builder()
                            .discovery(savedDiscovery)
                            .tag(req.tag())
                            .content(req.content())
                            .build();
                    subDiscoveryRepository.save(sub); // 추후 최적화 시 saveAll로 변경을 고려해볼 수 있습니다.
                    return new DiscoveryRes.SubDiscoveryResponse(sub.getId(), sub.getTag(), sub.getContent());
                })
                .collect(Collectors.toList());

        log.info("발견 등록 성공 - discoveryId: {}, 생성된 서브 발견 수: {}", savedDiscovery.getId(), subDiscoveryResponses.size());

        return new DiscoveryRes.DiscoveryResponse(
                savedDiscovery.getId(),
                trip.getId(),
                trip.getCountryCode().name(),
                trip.getCityCode().name(),
                savedDiscovery.getTravelType(),
                savedDiscovery.getCreatedAt(),
                subDiscoveryResponses
        );
    }

    /**
     * 발견 목록 조회 (최신순)
     */
    @Transactional(readOnly = true)
    public DiscoveryRes.DiscoveryListResponse getDiscoveries(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Discovery> discoveryPage = discoveryRepository.findAllWithTripAndMember(pageable);

        List<DiscoveryRes.DiscoveryListItemResponse> items = discoveryPage.getContent().stream()
                .map(d -> new DiscoveryRes.DiscoveryListItemResponse(
                        d.getId(),
                        d.getTrip().getCountryCode().name(),
                        d.getTrip().getCityCode().name(),
                        d.getTravelType().name(),
                        d.getMember().getName(),
                        d.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new DiscoveryRes.DiscoveryListResponse(
                items,
                discoveryPage.getTotalElements(),
                discoveryPage.getTotalPages(),
                discoveryPage.getNumber()
        );
    }

    /**
     * 내 발견 목록 전체 조회 (최신순)
     */
    @Transactional(readOnly = true)
    public List<DiscoveryRes.DiscoveryListItemResponse> getMyDiscoveries(Long memberId) {
        List<Discovery> discoveries = discoveryRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId);
        
        return discoveries.stream()
                .map(d -> new DiscoveryRes.DiscoveryListItemResponse(
                        d.getId(),
                        d.getTrip().getCountryCode().name(),
                        d.getTrip().getCityCode().name(),
                        d.getTravelType().name(),
                        d.getMember().getName(),
                        d.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 발견 상세 조회
     */
    @Transactional(readOnly = true)
    public DiscoveryRes.DiscoveryResponse getDiscovery(Long memberId, Long discoveryId) {
        Discovery discovery = discoveryRepository.findWithTripAndMemberById(discoveryId)
                .orElseThrow(() -> new IllegalArgumentException("발견 정보가 없습니다. (NOT_FOUND)"));

        // 본인 발견 또는 공개 발견이므로 별도 권한 체크 없이 조회 허용
        // (추후 비공개 기능이 생기면 여기서 memberId 검증 추가)

        List<SubDiscovery> subDiscoveries = subDiscoveryRepository.findByDiscoveryId(discoveryId);

        List<DiscoveryRes.SubDiscoveryResponse> subDiscoveryResponses = subDiscoveries.stream()
                .map(sub -> new DiscoveryRes.SubDiscoveryResponse(
                        sub.getId(),
                        sub.getTag(),
                        sub.getContent()
                ))
                .collect(Collectors.toList());

        Trip trip = discovery.getTrip();

        return new DiscoveryRes.DiscoveryResponse(
                discovery.getId(),
                trip.getId(),
                trip.getCountryCode().name(),
                trip.getCityCode().name(),
                discovery.getTravelType(),
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
    public List<DiscoveryRes.SubDiscoveryCardResponse> getSubDiscoveriesForTimeline(String countryCode, Tag tag) {
        Specification<SubDiscovery> spec = Specification
                .where(SubDiscoverySpecs.countryCodeEquals(countryCode))
                .and(SubDiscoverySpecs.tagEquals(tag));

        Pageable pageable = PageRequest.of(0, TIMELINE_CARD_LIMIT, Sort.by(Sort.Direction.DESC, "createdAt"));

        return subDiscoveryRepository.findAll(spec, pageable)
                .map(DiscoveryRes.SubDiscoveryCardResponse::from)
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

}
