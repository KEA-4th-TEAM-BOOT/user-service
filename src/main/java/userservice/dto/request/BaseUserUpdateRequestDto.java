package userservice.dto.request;

import jakarta.validation.constraints.NotNull;

public record BaseUserUpdateRequestDto(
     @NotNull String nickname,
     @NotNull String profileUrl,
     @NotNull String introduce,
     @NotNull Integer followingNum,
     @NotNull Integer followerNum,
     @NotNull Integer latestPostId
) {

}
