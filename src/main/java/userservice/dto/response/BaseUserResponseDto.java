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
        String introduce,
        String userLink,
        Integer followingNum,
        Integer followerNum,
        Integer latestPostId,
        Integer postCnt,
        String voiceModelUrl,
        List<CategoryResponseDto> categoryList,
        List<FollowResponseDto> followingList,
        List<FollowResponseDto> followerList,
        List<FollowResponseDto> nonFollowList
) {
    public static BaseUserResponseDto of(User user, List<CategoryResponseDto> categoryList, List<FollowResponseDto> followingList, List<FollowResponseDto> followerList, List<FollowResponseDto> nonFollowList) {
        return BaseUserResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .introduce(user.getIntroduce())
                .userLink(user.getUserLink())
                .followingNum(user.getFollowingNum())
                .followerNum(user.getFollowerNum())
                .latestPostId(user.getLatestPostId())
                .postCnt(user.getPostCnt())
                .voiceModelUrl(user.getVoiceModelUrl())
                .categoryList(categoryList)
                .followingList(followingList)
                .followerList(followerList)
                .nonFollowList(nonFollowList)
                .build();
    }
}
