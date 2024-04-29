package userservice.dto.response;

import java.util.List;

public record FollowListResponseDto (
        List<String> followerList,
        List<String> followedList
        ) {
}
