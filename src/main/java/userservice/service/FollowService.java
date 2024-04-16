package userservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import userservice.domain.Follow;
import userservice.domain.User;
import userservice.repository.FollowRepository;
import userservice.repository.UserRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public void createFollow(Long followerId, Long followedId) {
        User followerUser = userRepository.findById(followerId).orElseThrow();
        User followedUser = userRepository.findById(followedId).orElseThrow();
        Follow follow = Follow.createFollow(followerUser, followedUser);
    }
}
