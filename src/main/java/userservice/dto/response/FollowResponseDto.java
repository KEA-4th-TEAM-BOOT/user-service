package userservice.dto.response;


public record FollowResponseDto(
        String nickname,
        String email,
        String profileUrl,
        String userLink
) {
}
