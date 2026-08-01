package hajiton.chaebee.domain.discovery.service;

import hajiton.chaebee.domain.discovery.dto.DiscoveryReq;
import hajiton.chaebee.domain.discovery.dto.DiscoveryRes;
import hajiton.chaebee.domain.discovery.entity.Discovery;
import hajiton.chaebee.domain.discovery.entity.SubDiscovery;
import hajiton.chaebee.domain.discovery.entity.TravelType;
import hajiton.chaebee.domain.discovery.repository.DiscoveryRepository;
import hajiton.chaebee.domain.discovery.repository.SubDiscoveryRepository;
import hajiton.chaebee.domain.member.entity.Member;
import hajiton.chaebee.domain.member.repository.MemberRepository;
import hajiton.chaebee.domain.trip.entity.*;
import hajiton.chaebee.domain.trip.repository.ChecklistItemRepository;
import hajiton.chaebee.domain.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 타임라인 구성
    public DiscoveryRes.TimelineResponse getTimeline(Long tripId) {
        log.info("타임라인 구성 요청 시작 - tripId: {}", tripId);

        // 1. 여행 정보 및 Enum 데이터 세팅
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> {
                    log.warn("타임라인 구성 실패 - 존재하지 않는 여행 (tripId: {})", tripId);
                    return new IllegalArgumentException("존재하지 않는 여행입니다.");
                });

        City city = trip.getCityCode();
        Country country = city.getCountry();
        LocalDate departureDate = trip.getDepartureDate().toLocalDate();
        long currentDday = ChronoUnit.DAYS.between(LocalDate.now(), departureDate);

        // 2. 체크리스트 및 팁 데이터 조회
        List<ChecklistItem> checklists = checklistItemRepository.findAllByTripId(tripId);

        Optional<Discovery> optionalDiscovery = discoveryRepository.findByTripId(tripId);
        List<SubDiscovery> subDiscoveries = optionalDiscovery
                .map(discovery -> subDiscoveryRepository.findAllByDiscoveryId(discovery.getId()))
                .orElse(Collections.emptyList());

        log.debug("조회된 체크리스트 수: {}, 서브 발견 수: {}", checklists.size(), subDiscoveries.size());

        // 3. 상단 헤더 (TripInfo) 조립
        int totalChecklists = checklists.size();
        int completedChecklists = (int) checklists.stream().filter(ChecklistItem::getIsChecked).count();
        int percentage = totalChecklists == 0 ? 0 : (int) Math.round((double) completedChecklists / totalChecklists * 100);

        log.debug("체크리스트 진척도: {}/{} ({}%)", completedChecklists, totalChecklists, percentage);

        DiscoveryRes.Progress progress = new DiscoveryRes.Progress(totalChecklists, completedChecklists, percentage);
        DiscoveryRes.TripInfo tripInfo = new DiscoveryRes.TripInfo(
                city.getKoreanName() + ", " + country.getKoreanName(),
                currentDday,
                progress
        );

        // 4. 하단 필수 정보 (EssentialInfo) 조립
        DiscoveryRes.EssentialInfo essentialInfo = new DiscoveryRes.EssentialInfo(
                country.getPassportValidityRule(),
                country.getVisaFreeStayDays(),
                country.getOfficialSiteUrl(),
                country.getLastUpdatedAt()
        );

        // 5. D-Day 기준으로 데이터 그룹화
        Set<Integer> allDDays = new TreeSet<>();

        Map<Integer, List<DiscoveryRes.TimelineChecklist>> checklistMap = checklists.stream()
                .map(item -> {
                    Tag tagEnum = item.getTag();
                    allDDays.add(tagEnum.getDDay());
                    return new DiscoveryRes.TimelineChecklist(
                            item.getId(),
                            tagEnum,
                            tagEnum.getDescription(),
                            item.getIsChecked()
                    );
                })
                .collect(Collectors.groupingBy(dto -> dto.tag().getDDay()));

        Map<Integer, List<DiscoveryRes.TimelineDiscovery>> discoveryMap = subDiscoveries.stream()
                .map(sub -> {
                    Tag tagEnum = sub.getTag();
                    allDDays.add(tagEnum.getDDay());
                    return new DiscoveryRes.TimelineDiscovery(
                            tagEnum,
                            tagEnum.getDescription(),
                            sub.getContent()
                    );
                })
                .collect(Collectors.groupingBy(dto -> dto.tag().getDDay()));

        // 6. TimelineGroup 리스트 조립
        List<DiscoveryRes.TimelineGroup> timelineGroups = allDDays.stream()
                .map(dDay -> {
                    LocalDate targetDate = departureDate.plusDays(dDay);
                    return new DiscoveryRes.TimelineGroup(
                            dDay,
                            targetDate,
                            discoveryMap.getOrDefault(dDay, Collections.emptyList()),
                            checklistMap.getOrDefault(dDay, Collections.emptyList())
                    );
                })
                .toList();

        log.info("타임라인 구성 완료 - 총 D-Day 그룹 수: {}", timelineGroups.size());

        return new DiscoveryRes.TimelineResponse(tripInfo, timelineGroups, essentialInfo);
    }
}