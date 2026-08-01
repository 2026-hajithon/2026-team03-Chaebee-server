package hajiton.chaebee.domain.member.controller;

import hajiton.chaebee.domain.member.dto.MemberReq;
import hajiton.chaebee.domain.member.dto.MemberRes;
import hajiton.chaebee.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member API", description = "회원 관련 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 소셜 로그인 / 게스트 로그인
    @Operation(summary = "로그인", description = "소셜 로그인 또는 게스트 로그인을 수행합니다.")
    @PostMapping("/login")
    public MemberRes.Login login(@RequestBody MemberReq.Login request) {

        // 공통 응답 객체 없이, 서비스에서 처리한 결과(DTO)를 그대로 프론트에 던져줌!
        return memberService.login(request);
    }
}
