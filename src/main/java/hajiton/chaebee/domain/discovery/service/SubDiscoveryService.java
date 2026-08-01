package hajiton.chaebee.domain.discovery.service;

import hajiton.chaebee.domain.discovery.entity.DiscoveryAssignment;
import hajiton.chaebee.domain.discovery.entity.SubDiscovery;
import hajiton.chaebee.domain.discovery.repository.DiscoveryAssignmentRepository;
import hajiton.chaebee.domain.discovery.repository.SubDiscoveryRepository;
import hajiton.chaebee.domain.member.entity.Member;
import hajiton.chaebee.domain.member.repository.MemberRepository;
import hajiton.chaebee.domain.trip.entity.Country;
import hajiton.chaebee.domain.trip.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubDiscoveryService {

    private final SubDiscoveryRepository subDiscoveryRepository;
    private final DiscoveryAssignmentRepository discoveryAssignmentRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<SubDiscoveryResponse> getSubDiscoveries(String countryCodeStr, String tagStr) {
        Country countryCode = (countryCodeStr != null && !countryCodeStr.isBlank())
                ? Country.valueOf(countryCodeStr.toUpperCase()) : null;
        Tag tag = (tagStr != null && !tagStr.isBlank())
                ? Tag.valueOf(tagStr.toUpperCase()) : null;

        PageRequest pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        Page<SubDiscovery> page = subDiscoveryRepository.findAllByFilter(countryCode, tag, pageable);
        
        return page.getContent().stream()
                .map(sd -> new SubDiscoveryResponse(
                        sd.getId(),
                        sd.getTag().name(),
                        sd.getContent(),
                        sd.getDiscovery().getMember().getName()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public DailySubDiscoveryResponse getDailySubDiscoveries(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        LocalDate today = LocalDate.now();

        // 1. 이미 오늘 배정받은 서브 발견이 있는지 확인
        List<DiscoveryAssignment> todayAssignments = discoveryAssignmentRepository.findByMemberIdAndAssignedDate(memberId, today);

        if (!todayAssignments.isEmpty()) {
            List<SubDiscoveryResponse> responses = todayAssignments.stream()
                    .map(DiscoveryAssignment::getSubDiscovery)
                    .map(sd -> new SubDiscoveryResponse(
                            sd.getId(),
                            sd.getTag().name(),
                            sd.getContent(),
                            sd.getDiscovery().getMember().getName()
                    ))
                    .collect(Collectors.toList());
            return new DailySubDiscoveryResponse(today, responses);
        }

        // 2. 오늘 배정받은 것이 없다면 새로운 서브 발견 배정 (아직 배정받지 않은 것들 중 3개)
        PageRequest pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<SubDiscovery> unassigned = subDiscoveryRepository.findUnassignedSubDiscoveries(memberId, pageable);

        // 3. 배정 내역 저장
        for (SubDiscovery sd : unassigned) {
            DiscoveryAssignment assignment = DiscoveryAssignment.builder()
                    .subDiscovery(sd)
                    .member(member)
                    .assignedDate(today)
                    .build();
            discoveryAssignmentRepository.save(assignment);
        }

        List<SubDiscoveryResponse> responses = unassigned.stream()
                .map(sd -> new SubDiscoveryResponse(
                        sd.getId(),
                        sd.getTag().name(),
                        sd.getContent(),
                        sd.getDiscovery().getMember().getName()
                ))
                .collect(Collectors.toList());

        return new DailySubDiscoveryResponse(today, responses);
    }

    public record SubDiscoveryResponse(
            Long subDiscoveryId,
            String tag,
            String content,
            String authorName
    ) {}

    public record DailySubDiscoveryResponse(
            LocalDate deliveredDate,
            List<SubDiscoveryResponse> subDiscoveries
    ) {}
}
