package hajiton.chaebee.trip.controller;

import hajiton.chaebee.common.dto.ApiResponse;
import hajiton.chaebee.trip.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checklist-items")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;

    @PatchMapping("/{checklistItemId}")
    public ApiResponse<?> updateChecklistItem(
            @PathVariable Long checklistItemId, 
            @RequestBody UpdateChecklistRequest request) {
        
        Long dummyMemberId = 1L; // TODO: 시큐리티/인터셉터에서 추출
        checklistService.updateChecklistItem(dummyMemberId, checklistItemId, request.isChecked());
        
        return ApiResponse.success(null);
    }

    public record UpdateChecklistRequest(Boolean isChecked) {}
}
