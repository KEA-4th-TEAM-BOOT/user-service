package userservice.dto.response;

import lombok.Builder;

@Builder
public record LoginResponseDto(
        TokenResponseDto tokenResponseDto,
        Long userId,
        String userLink,
        String profileUrl
) {
}
