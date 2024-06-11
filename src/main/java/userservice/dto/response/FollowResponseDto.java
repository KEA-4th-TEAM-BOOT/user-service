package userservice.dto.response;


public record FollowResponseDto(
        Long userId,
        String nickname,
        String email,
        String profileUrl,
        String userLink,
        String introduce
) {
}
