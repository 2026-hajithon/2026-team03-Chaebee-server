package hajiton.chaebee.domain.trip.service;

import hajiton.chaebee.domain.trip.entity.ChecklistItem;
import hajiton.chaebee.domain.trip.repository.ChecklistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final ChecklistItemRepository checklistItemRepository;

    @Transactional
    public void updateChecklistItem(Long memberId, Long checklistItemId, Boolean isChecked) {
        ChecklistItem item = checklistItemRepository.findById(checklistItemId)
                .orElseThrow(() -> new IllegalArgumentException("체크리스트 항목을 찾을 수 없습니다."));

        // 소유권 검증
        if (!item.getTrip().getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다. (FORBIDDEN)");
        }

        item.changeIsChecked(isChecked);
    }
}
