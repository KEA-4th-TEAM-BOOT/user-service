package userservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import userservice.domain.Follow;
import userservice.domain.User;
import userservice.repository.FollowRepository;
import userservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public void createFollow(Long followerId, Long followedId) {
        User followerUser = userRepository.findById(followerId).orElseThrow();
        User followedUser = userRepository.findById(followedId).orElseThrow();
        Follow.createFollow(followerUser, followedUser);
    }

    public List<String> getFollowerList(Long follower_id) {
        User user = userRepository.findById(follower_id).orElseThrow();
        List <String> followerList = user.getFollowerList().stream()
                .map(follower -> follower.getFollowedUser().getName())
                .toList();
        return followerList;
    }

    public List<String> getFollowedList(Long followed_id) {
        User user = userRepository.findById(followed_id).orElseThrow();
        List <String> followedList = user.getFollowedList().stream()
                .map(followed -> followed.getFollowerUser().getName())
                .toList();
        return followedList;
    }


    public void deleteFollow(Long followerId, Long followedId) {
        followRepository.deleteFollowByFollowerUserIdAndFollowedUserId(followerId, followedId);
    }
}

