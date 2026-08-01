package hajiton.chaebee.member.controller;

import hajiton.chaebee.common.dto.ApiResponse;
import hajiton.chaebee.member.domain.LoginProvider;
import hajiton.chaebee.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest request) {
        // 구글 로그인 우선 적용 (추후 애플 등 확장 가능)
        Object result = memberService.login(request.provider(), request.providerToken());
        return ApiResponse.success(result);
    }

    @GetMapping("/me")
    public ApiResponse<?> getMe() {
        // TODO: 내 정보 조회 로직 구현
        return ApiResponse.success(null);
    }

    public record LoginRequest(LoginProvider provider, String providerToken) {}
}
