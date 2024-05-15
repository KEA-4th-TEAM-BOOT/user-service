package userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import userservice.domain.Follow;
import userservice.domain.User;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteFollowByFollowingUserAndFollowerUser(User followingUser, User followerUser);
}
