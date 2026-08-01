package hajiton.chaebee.domain.discovery.dto;

import hajiton.chaebee.domain.discovery.entity.TravelType;
import hajiton.chaebee.domain.trip.entity.Tag;

import java.time.LocalDate;
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
            TravelType tripType,
            LocalDateTime createdAt,
            List<SubDiscoveryResponse> subDiscoveries
    ) {}

    public record SubDiscoveryResponse(
            Long subDiscoveryId,
            Tag tag,
            String content
    ) {}


    // ==========================================
    public record TimelineResponse(
            TripInfo tripInfo,
            List<TimelineGroup> timeline,
            EssentialInfo essentialInfo // 하단 필수 정보 추가
    ) {}

    public record TripInfo(
            String destination,
            long dDay,
            Progress progress
    ) {}

    public record Progress(
            int total,
            int completed,
            int percentage
    ) {}

    public record TimelineGroup(
            int dDay,
            LocalDate date,
            List<TimelineDiscovery> discoveries,
            List<TimelineChecklist> checklists
    ) {}

    public record TimelineDiscovery(
            Tag tag,
            String title,
            String content
    ) {}

    public record TimelineChecklist(
            Long checklistId,
            Tag tag,
            String title,
            boolean isChecked
    ) {}

    // Country Enum에서 가져올 하단 필수 정보 영역
    public record EssentialInfo(
            String passportValidityRule,
            Integer visaFreeStayDays,
            String officialSiteUrl,
            LocalDate lastUpdatedAt
    ) {}
}
