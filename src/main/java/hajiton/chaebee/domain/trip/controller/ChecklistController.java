package hajiton.chaebee.domain.trip.controller;

import hajiton.chaebee.domain.dto.ApiResponse;
import hajiton.chaebee.domain.trip.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checklist-items")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;

    @PatchMapping("/{checklistItemId}")
    public ApiResponse<?> updateChecklistItem(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long checklistItemId,
            @RequestBody UpdateChecklistRequest request) {

        checklistService.updateChecklistItem(memberId, checklistItemId, request.isChecked());

        return ApiResponse.success(null);
    }

    public record UpdateChecklistRequest(Boolean isChecked) {}
}