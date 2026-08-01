package hajiton.chaebee.member.controller;

import hajiton.chaebee.member.dto.MemberReq;
import hajiton.chaebee.member.dto.MemberRes;
import hajiton.chaebee.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 1.1 소셜 로그인 / 게스트 로그인
    @PostMapping("/login")
    public MemberRes.Login login(@RequestBody MemberReq.Login request) {

        // 공통 응답 객체 없이, 서비스에서 처리한 결과(DTO)를 그대로 프론트에 던져줌!
        return memberService.login(request);
    }

}
