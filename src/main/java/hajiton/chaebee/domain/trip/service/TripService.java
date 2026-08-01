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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TripResponse createTrip(Long memberId, String countryCodeStr, String cityCodeStr,
                                   LocalDateTime departureAt, LocalDateTime arrivalAt,
                                   Boolean esimPlan, Boolean cashPlan) {
        
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 기존에 진행 중인 여행이 있는지 확인 로직 추가 필요 (DUPLICATED_ACTIVE_TRIP)
        // 임시로 그냥 넘기거나 예외 처리 가능
        // if (tripRepository.existsByMemberIdAndArrivalDateAfter(memberId, LocalDateTime.now())) {
        //     throw new IllegalStateException("이미 진행중인 여행이 있습니다.");
        // }

        Country country = Country.valueOf(countryCodeStr);
        City city = City.valueOf(cityCodeStr);

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

        // 도시에 맞는 필수 준비물 태그들을 기반으로 체크리스트 생성
        // (City enum에서 전체 태그를 가져와서 ChecklistItem으로 저장)
        // City 클래스의 구조에 따라 전체 태그를 순회하며 저장
        for (Tag tag : Tag.values()) {
            // 해당 도시에 적용 가능한 태그인지 확인하는 필터링 로직이 있다면 여기서 분기
            ChecklistItem item = ChecklistItem.builder()
                    .trip(savedTrip)
                    .tag(tag)
                    .dDay(tag.getDDay())
                    .isChecked(false)
                    .build();
            checklistItemRepository.save(item);
        }

        long dDay = ChronoUnit.DAYS.between(LocalDateTime.now(), departureAt);

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
    public TripResponse getMyTrip(Long memberId) {
        // 본인의 진행 중인 여행 하나를 가져온다고 가정
        Trip trip = tripRepository.findFirstByMemberIdOrderByDepartureDateDesc(memberId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 여행이 없습니다. (TRIP_NOT_FOUND)"));

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
    }

    @Transactional
    public void deleteTrip(Long memberId, Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("여행 정보가 없습니다."));
        
        if (!trip.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다. (FORBIDDEN)");
        }
        
        // ChecklistItem 등 연관 데이터 삭제 로직 (보통 CASCADE로 처리하거나 여기서 수동 삭제)
        tripRepository.delete(trip);
    }

    @Transactional(readOnly = true)
    public TripResponse getTrip(Long memberId, Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("여행 정보가 없습니다. (TRIP_NOT_FOUND)"));
        
        if (!trip.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("조회 권한이 없습니다. (FORBIDDEN)");
        }

        long dDay = ChronoUnit.DAYS.between(LocalDateTime.now(), trip.getDepartureDate());
        
        return new TripResponse(
                trip.getId(), trip.getCountryCode().name(), trip.getCityCode().name(),
                trip.getDepartureDate(), trip.getArrivalDate(), trip.getHasEsim(), trip.getHasCash(), (int) dDay
        );
    }

    @Transactional
    public void updateTrip(Long memberId, Long tripId, Object request) {
        // TODO: 요청 DTO 기반 필드 수정 로직 (날짜 변경 등)
    }

    @Transactional(readOnly = true)
    public Object getTimeline(Long memberId, Long tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new IllegalArgumentException("TRIP_NOT_FOUND"));
        if (!trip.getMember().getId().equals(memberId)) throw new IllegalArgumentException("FORBIDDEN");

        List<ChecklistItem> items = checklistItemRepository.findByTripIdOrderByDDayDesc(tripId);
        
        // 간단히 항목 목록만 반환 (추후 D-Day별 그룹핑 로직 필요)
        return items.stream().map(item -> new ChecklistItemDto(
                item.getId(),
                item.getTag().name(),
                item.getTag().getDescription(),
                item.getDDay(),
                item.getIsChecked()
        )).toList();
    }

    // Response DTO
    public record TripResponse(
            Long tripId,
            String countryCode,
            String cityCode,
            LocalDateTime departureAt,
            LocalDateTime arrivalAt,
            Boolean esimPlan,
            Boolean cashPlan,
            Integer dDay
    ) {}
    
    public record ChecklistItemDto(Long checklistItemId, String tag, String title, int dDay, boolean isChecked) {}
}
