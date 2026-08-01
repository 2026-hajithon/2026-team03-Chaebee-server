package hajiton.chaebee.domain.trip.controller;

import hajiton.chaebee.domain.common.dto.ApiResponse;
import hajiton.chaebee.domain.trip.dto.ChecklistReq;
import hajiton.chaebee.domain.trip.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "Checklist API", description = "체크리스트 관련 API")
@RestController
@RequestMapping("/api/checklist-items")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;

    @Operation(summary = "체크리스트 상태 변경", description = "특정 체크리스트 항목의 체크 상태를 변경합니다.")
    @PatchMapping("/{checklistItemId}")
    public ResponseEntity<ApiResponse<Void>> updateChecklistItem(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long checklistItemId,
            @RequestBody ChecklistReq.UpdateChecklistRequest request) {

        checklistService.updateChecklistItem(memberId, checklistItemId, request.isChecked());

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}