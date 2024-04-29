package userservice.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import userservice.domain.User;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record BaseUserResponseDto(
        String name,
        String email,
        String nickname,
        String profileUrl,
        String blogUrl,
        Integer followingNum,
        Integer followerNum,
        Integer latestPostId,
        List<CategoryResponseDto> categoryList
) {
    public static BaseUserResponseDto of(User user, List<CategoryResponseDto> categoryList){
        return BaseUserResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .blogUrl(user.getBlogUrl())
                .followingNum(user.getFollowingNum())
                .followerNum(user.getFollowerNum())
                .latestPostId(user.getLatestPostId())
                .categoryList(categoryList)
                .build();
    }
}
