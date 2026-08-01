package hajiton.chaebee.domain.trip.service;

import hajiton.chaebee.domain.member.entity.Member;
import hajiton.chaebee.domain.member.repository.MemberRepository;
import hajiton.chaebee.domain.trip.entity.ChecklistItem;
import hajiton.chaebee.domain.trip.entity.City;
import hajiton.chaebee.domain.trip.entity.Country;
import hajiton.chaebee.domain.trip.entity.Tag;
import hajiton.chaebee.domain.trip.entity.Trip;
import hajiton.chaebee.domain.trip.repository.ChecklistItemRepository;
import hajiton.chaebee.domain.trip.repository.TripRepository;
import hajiton.chaebee.domain.trip.dto.TripRes.TripResponse;
import hajiton.chaebee.domain.trip.dto.TripRes.ChecklistItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final MemberRepository memberRepository;
    private final hajiton.chaebee.domain.discovery.repository.DiscoveryRepository discoveryRepository;
    private final hajiton.chaebee.domain.discovery.repository.SubDiscoveryRepository subDiscoveryRepository;
    private final hajiton.chaebee.domain.discovery.repository.DiscoveryAssignmentRepository discoveryAssignmentRepository;

    @Transactional
    public TripResponse createTrip(Long memberId, String countryCodeStr, String cityCodeStr,
                                   LocalDateTime departureAt, LocalDateTime arrivalAt,
                                   Boolean esimPlan, Boolean cashPlan) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 1. 기존에 진행 중인 여행이 있는지 확인 (활성화 완료)
        if (tripRepository.existsByMemberIdAndArrivalDateAfter(memberId, LocalDateTime.now())) {
            throw new IllegalStateException("이미 진행중인 여행이 있습니다. (DUPLICATED_ACTIVE_TRIP)");
        }

        Country country = Country.valueOf(countryCodeStr);
        City city = City.valueOf(cityCodeStr);

        // 2. 여행 객체 생성 및 저장
        Trip trip = Trip.builder()
                .member(member)
                .countryCode(country)
                .cityCode(city)
                .departureDate(departureAt)
                .arrivalDate(arrivalAt)
                .hasEsim(esimPlan)
                .hasCash(cashPlan)
                .build();

        Trip savedTrip = tripRepository.save(trip);

        // 3. 도시에 맞는 태그 필터링 및 체크리스트 일괄 저장 (Batch Insert로 N+1 방지)
        List<ChecklistItem> checklistItems = city.getApplicableTags().stream()
                .map(tag -> ChecklistItem.builder()
                        .trip(savedTrip)
                        .tag(tag)
                        .dDay(tag.getDDay())
                        .isChecked(false)
                        .build())
                .toList();

        checklistItemRepository.saveAll(checklistItems);

        // 4. 시간 오차 방지를 위해 LocalDate로 변환하여 정확한 D-Day 날짜 계산
        long dDay = ChronoUnit.DAYS.between(
                LocalDate.now(),
                departureAt.toLocalDate()
        );

        return new TripResponse(
                savedTrip.getId(),
                savedTrip.getCountryCode().name(),
                savedTrip.getCityCode().name(),
                savedTrip.getDepartureDate(),
                savedTrip.getArrivalDate(),
                savedTrip.getHasEsim(),
                savedTrip.getHasCash(),
                (int) dDay
        );
    }

    @Transactional(readOnly = true)
    public List<TripResponse> getMyTrip(Long memberId) {
        // 본인의 모든 여행 목록을 출발일 기준 내림차순으로 가져옴
        List<Trip> trips = tripRepository.findAllByMemberIdOrderByDepartureDateDesc(memberId);

        if (trips.isEmpty()) {
            throw new IllegalArgumentException("등록된 여행이 없습니다. (TRIP_NOT_FOUND)");
        }

        return trips.stream().map(trip -> {
            long dDay = ChronoUnit.DAYS.between(LocalDateTime.now(), trip.getDepartureDate());
            return new TripResponse(
                    trip.getId(),
                    trip.getCountryCode().name(),
                    trip.getCityCode().name(),
                    trip.getDepartureDate(),
                    trip.getArrivalDate(),
                    trip.getHasEsim(),
                    trip.getHasCash(),
                    (int) dDay
            );
        }).toList();
    }

    @Transactional
    public void deleteTrip(Long memberId, Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("여행 정보가 없습니다."));
        
        if (!trip.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다. (FORBIDDEN)");
        }
        
        // 1. ChecklistItem 삭제
        checklistItemRepository.deleteByTripId(tripId);

        // 2. Discovery 연관 데이터 삭제 (Discovery -> SubDiscovery -> DiscoveryAssignment)
        discoveryRepository.findByTripId(tripId).ifPresent(discovery -> {
            List<hajiton.chaebee.domain.discovery.entity.SubDiscovery> subDiscoveries = subDiscoveryRepository.findByDiscoveryId(discovery.getId());
            if (!subDiscoveries.isEmpty()) {
                List<Long> subDiscoveryIds = subDiscoveries.stream().map(hajiton.chaebee.domain.discovery.entity.SubDiscovery::getId).toList();
                discoveryAssignmentRepository.deleteBySubDiscoveryIdIn(subDiscoveryIds);
                subDiscoveryRepository.deleteByDiscoveryId(discovery.getId());
            }
            discoveryRepository.deleteByTripId(tripId);
        });

        // 3. Trip 삭제
        tripRepository.delete(trip);
    }

    @Transactional(readOnly = true)
    public hajiton.chaebee.domain.trip.dto.TripRes.TimelineResponse getTimeline(Long memberId, Long tripId) {
        // 1. 여행 정보 및 Enum 데이터 세팅
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new IllegalArgumentException("TRIP_NOT_FOUND"));
        if (!trip.getMember().getId().equals(memberId)) throw new IllegalArgumentException("FORBIDDEN");

        City city = trip.getCityCode();
        Country country = city.getCountry();
        LocalDate departureDate = trip.getDepartureDate().toLocalDate();
        long currentDday = ChronoUnit.DAYS.between(LocalDate.now(), departureDate);

        // 2. 체크리스트 및 팁 데이터 조회
        List<ChecklistItem> checklists = checklistItemRepository.findAllByTripId(tripId);

        java.util.Optional<hajiton.chaebee.domain.discovery.entity.Discovery> optionalDiscovery = discoveryRepository.findByTripId(tripId);
        List<hajiton.chaebee.domain.discovery.entity.SubDiscovery> subDiscoveries = optionalDiscovery
                .map(discovery -> subDiscoveryRepository.findByDiscoveryId(discovery.getId()))
                .orElse(java.util.Collections.emptyList());

        // 3. 상단 헤더 (TripInfo) 조립
        int totalChecklists = checklists.size();
        int completedChecklists = (int) checklists.stream().filter(ChecklistItem::getIsChecked).count();
        int percentage = totalChecklists == 0 ? 0 : (int) Math.round((double) completedChecklists / totalChecklists * 100);

        hajiton.chaebee.domain.trip.dto.TripRes.Progress progress = new hajiton.chaebee.domain.trip.dto.TripRes.Progress(totalChecklists, completedChecklists, percentage);
        hajiton.chaebee.domain.trip.dto.TripRes.TripInfo tripInfo = new hajiton.chaebee.domain.trip.dto.TripRes.TripInfo(
                city.getKoreanName() + ", " + country.getKoreanName(),
                currentDday,
                progress
        );

        // 4. 하단 필수 정보 (EssentialInfo) 조립
        hajiton.chaebee.domain.trip.dto.TripRes.EssentialInfo essentialInfo = new hajiton.chaebee.domain.trip.dto.TripRes.EssentialInfo(
                country.getPassportValidityRule(),
                country.getVisaFreeStayDays(),
                country.getOfficialSiteUrl(),
                country.getLastUpdatedAt()
        );

        // 5. D-Day 기준으로 데이터 그룹화
        java.util.Set<Integer> allDDays = new java.util.TreeSet<>();

        java.util.Map<Integer, List<hajiton.chaebee.domain.trip.dto.TripRes.TimelineChecklist>> checklistMap = checklists.stream()
                .map(item -> {
                    Tag tagEnum = item.getTag();
                    allDDays.add(tagEnum.getDDay());
                    return new hajiton.chaebee.domain.trip.dto.TripRes.TimelineChecklist(
                            item.getId(),
                            tagEnum,
                            tagEnum.getDescription(),
                            item.getIsChecked()
                    );
                })
                .collect(java.util.stream.Collectors.groupingBy(dto -> dto.tag().getDDay()));

        java.util.Map<Integer, List<hajiton.chaebee.domain.trip.dto.TripRes.TimelineDiscovery>> discoveryMap = subDiscoveries.stream()
                .map(sub -> {
                    Tag tagEnum = sub.getTag();
                    allDDays.add(tagEnum.getDDay());
                    return new hajiton.chaebee.domain.trip.dto.TripRes.TimelineDiscovery(
                            tagEnum,
                            tagEnum.getDescription(),
                            sub.getContent()
                    );
                })
                .collect(java.util.stream.Collectors.groupingBy(dto -> dto.tag().getDDay()));

        // 6. TimelineGroup 리스트 조립
        List<hajiton.chaebee.domain.trip.dto.TripRes.TimelineGroup> timelineGroups = allDDays.stream()
                .map(dDay -> {
                    LocalDate targetDate = departureDate.plusDays(dDay);
                    return new hajiton.chaebee.domain.trip.dto.TripRes.TimelineGroup(
                            dDay,
                            targetDate,
                            discoveryMap.getOrDefault(dDay, java.util.Collections.emptyList()),
                            checklistMap.getOrDefault(dDay, java.util.Collections.emptyList())
                    );
                })
                .toList();

        return new hajiton.chaebee.domain.trip.dto.TripRes.TimelineResponse(tripInfo, timelineGroups, essentialInfo);
    }
}
