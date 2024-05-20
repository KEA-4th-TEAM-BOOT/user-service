package userservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import userservice.config.JwtTokenProvider;
import userservice.domain.Follow;
import userservice.domain.User;
import userservice.repository.FollowRepository;
import userservice.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void createFollow(String token, String userLink) {
        String accessToken = token.substring(7);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));

        User followerUser = userRepository.findById(userId).orElseThrow();
        User followedUser = userRepository.findByUserLink(userLink);
        Follow.createFollow(followerUser, followedUser);
    }

    public List<String> getFollowingList(HttpServletRequest httpServletRequest) {
        String accessToken = jwtTokenProvider.resolveToken(httpServletRequest);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));
        User user = userRepository.findById(userId).orElseThrow();
        return user.getFollowingList().stream()
                .map(follower -> follower.getFollowerUser().getName())
                .toList();
    }

    public List<String> getFollowedList(HttpServletRequest httpServletRequest) {
        String accessToken = jwtTokenProvider.resolveToken(httpServletRequest);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));
        User user = userRepository.findById(userId).orElseThrow();
        return user.getFollowingList().stream()
                .map(followed -> followed.getFollowerUser().getName())
                .toList();
    }

    public void deleteFollow(String token, String userLink) {
        String accessToken = token.substring(7);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));
        User followingUser = userRepository.findById(userId).orElseThrow();
        User followerUser = userRepository.findByUserLink(userLink);
        followRepository.deleteFollowByFollowingUserAndFollowerUser(followingUser, followerUser);
    }
}

