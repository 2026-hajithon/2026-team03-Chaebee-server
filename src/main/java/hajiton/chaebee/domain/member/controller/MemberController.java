package hajiton.chaebee.domain.member.controller;

import hajiton.chaebee.domain.member.dto.MemberReq;
import hajiton.chaebee.domain.member.dto.MemberRes;
import hajiton.chaebee.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 소셜 로그인 / 게스트 로그인
    @PostMapping("/login")
    public MemberRes.Login login(@RequestBody MemberReq.Login request) {

        return memberService.login(request);
    }

}
