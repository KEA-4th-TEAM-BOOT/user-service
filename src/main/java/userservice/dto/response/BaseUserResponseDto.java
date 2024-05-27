package userservice.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import userservice.domain.User;
import userservice.domain.Follow;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record BaseUserResponseDto(
        String name,
        String email,
        String nickname,
        String profileUrl,
        String userLink,
        Integer followingNum,
        Integer followerNum,
        Integer latestPostId,
        Integer postCnt,
        String voiceModelUrl,
        List<CategoryResponseDto> categoryList,
        List<FollowResponseDto> followingList,
        List<FollowResponseDto> followerList
) {
    public static BaseUserResponseDto of(User user, List<CategoryResponseDto> categoryList, List<FollowResponseDto> followingList, List<FollowResponseDto> followerList) {
        return BaseUserResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .userLink(user.getUserLink())
                .followingNum(user.getFollowingNum())
                .followerNum(user.getFollowerNum())
                .latestPostId(user.getLatestPostId())
                .postCnt(user.getPostCnt())
                .voiceModelUrl(user.getVoiceModelUrl())
                .categoryList(categoryList)
                .followingList(followingList)
                .followerList(followerList)
                .build();
    }
}
