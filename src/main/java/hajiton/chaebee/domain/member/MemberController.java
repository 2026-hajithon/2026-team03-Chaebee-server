package hajiton.chaebee.domain.member;

import hajiton.chaebee.domain.member.dto.MemberReq;
import hajiton.chaebee.domain.member.dto.MemberRes;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 1.1 소셜 로그인 / 게스트 로그인
    @PostMapping("/members/login")
    public MemberRes.Login login(@RequestBody MemberReq.Login request) {

        // 공통 응답 객체 없이, 서비스에서 처리한 결과(DTO)를 그대로 프론트에 던져줌!
        return memberService.login(request);
    }
}
