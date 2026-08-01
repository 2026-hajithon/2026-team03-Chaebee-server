package hajiton.chaebee.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 JSON 응답에서 제외합니다.
public class ApiResponse<T> {
    private int status;
    private boolean success;
    private String code;     // 실패 시에만 포함됨 (성공 시 null이라 안 보임)
    private String message;
    private T data;          // 성공 시에만 포함됨 (실패 시 null이라 안 보임)

    // 성공 응답 (code = null)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, true, null, "요청이 성공했습니다.", data);
    }

    // 에러 응답 (data = null)
    public static <T> ApiResponse<T> error(int status, String code, String message) {
        return new ApiResponse<>(status, false, code, message, null);
    }
}
