package userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import userservice.domain.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteFollowByFollowerUserIdAndFollowedUserId(Long followerUser_id, Long followedUser_id);
}
