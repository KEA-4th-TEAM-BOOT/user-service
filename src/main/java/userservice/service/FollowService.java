package userservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import userservice.config.JwtTokenProvider;
import userservice.domain.Follow;
import userservice.domain.User;
import userservice.dto.response.FollowResponseDto;
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

    public List<FollowResponseDto> getFollowingList(HttpServletRequest httpServletRequest) {
        String accessToken = jwtTokenProvider.resolveToken(httpServletRequest);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));
        User user = userRepository.findById(userId).orElseThrow();
        return user.getFollowingList().stream()
                .map(follower -> new FollowResponseDto(
                        follower.getFollowerUser().getId(),
                        follower.getFollowerUser().getNickname(),
                        follower.getFollowerUser().getEmail(),
                        follower.getFollowerUser().getProfileUrl(),
                        follower.getFollowingUser().getUserLink())
                )
                .toList();
    }

    public List<FollowResponseDto> getFollowedList(HttpServletRequest httpServletRequest) {
        String accessToken = jwtTokenProvider.resolveToken(httpServletRequest);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));
        User user = userRepository.findById(userId).orElseThrow();
        return user.getFollowingList().stream()
                .map(follower -> new FollowResponseDto(
                        follower.getFollowingUser().getId(),
                        follower.getFollowerUser().getNickname(),
                        follower.getFollowerUser().getEmail(),
                        follower.getFollowerUser().getProfileUrl(),
                        follower.getFollowingUser().getUserLink())
                )
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

