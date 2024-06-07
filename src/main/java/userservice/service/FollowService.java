package userservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import userservice.config.JwtTokenProvider;
import userservice.domain.Follow;
import userservice.domain.User;
import userservice.dto.request.BaseUserUpdateRequestDto;
import userservice.dto.response.FollowResponseDto;
import userservice.exception.user.UserNotFoundException;
import userservice.repository.FollowRepository;
import userservice.repository.UserRepository;
import userservice.utils.TokenUtils;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenUtils tokenUtils;

    public void createFollow(String token, String userLink) {
        Long userId = tokenUtils.getUserIdFromToken(token);

        User followingUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Follower user not found with id: " + userId));
        User followerUser = userRepository.findByUserLink(userLink).orElseThrow(() -> new UserNotFoundException("Followed user not found with userLink: " + userLink));
        Follow.createFollow(followingUser, followerUser);
    }

    public List<FollowResponseDto> getFollowingList(HttpServletRequest httpServletRequest) {
        Long userId = tokenUtils.getUserIdFromToken(jwtTokenProvider.resolveToken(httpServletRequest));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
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

    public List<FollowResponseDto> getFollowerList(HttpServletRequest httpServletRequest) {
        Long userId = tokenUtils.getUserIdFromToken(jwtTokenProvider.resolveToken(httpServletRequest));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return user.getFollowerList().stream()
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
        Long userId = tokenUtils.getUserIdFromToken(token);
        User followingUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Follower user not found with id: " + userId));
        User followerUser = userRepository.findByUserLink(userLink).orElseThrow(() -> new UserNotFoundException("Followed user not found with userLink: " + userLink));
        followRepository.deleteFollowByFollowingUserAndFollowerUser(followingUser, followerUser);
        followingUser.updateUser(new BaseUserUpdateRequestDto(null, null, null, followingUser.getFollowingNum() - 1, null, null, null, null));
        followerUser.updateUser(new BaseUserUpdateRequestDto(null, null, null, followerUser.getFollowerNum() - 1, null, null, null, null));
    }
}

