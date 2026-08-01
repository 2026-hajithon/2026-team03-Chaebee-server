package hajiton.chaebee.member.domain;

import hajiton.chaebee.apiPayload.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_WITHDRAWN(HttpStatus.CONFLICT, "USER_ALREADY_WITHDRAWN", "이미 탈퇴한 사용자입니다."),
    INVALID_PROVIDER_TOKEN(HttpStatus.NOT_FOUND, "INVALID_PROVIDER_TOKEN", "잘못된 토큰입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
