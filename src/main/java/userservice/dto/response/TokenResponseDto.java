package userservice.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder
public record TokenResponseDto(
        Integer code,
        String message,
        String accessToken,
        String refreshToken
) {
}
