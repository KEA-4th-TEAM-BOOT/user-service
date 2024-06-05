package userservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import userservice.config.JwtTokenProvider;
import userservice.controller.SubCategoryController;
import userservice.domain.Follow;
import userservice.domain.User;
import userservice.dto.request.BaseUserUpdateRequestDto;
import userservice.dto.response.CategoryResponseDto;
import userservice.dto.response.BaseUserResponseDto;
import userservice.dto.response.FollowResponseDto;
import userservice.repository.UserRepository;
import userservice.utils.TokenUtils;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final SubCategoryController subCategoryController;
    private final SubCategoryService subCategoryService;
    private final TokenUtils tokenUtils;

    public void deleteUser(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        log.info("Resolved token: {}", token);

        Long userId = tokenUtils.getUserIdFromToken(token);
        log.info("Extracted userId: {}", userId);

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("User found, proceeding to delete");
        userRepository.deleteById(userId);
    }

    public void updateUser(String token, BaseUserUpdateRequestDto baseUserUpdateRequestDto) {
        Long userId = tokenUtils.getUserIdFromToken(token);
        User user = userRepository.findById(userId).orElseThrow();
        user.updateUser(baseUserUpdateRequestDto);
    }

    public BaseUserResponseDto getUser(HttpServletRequest request) {
        Long userId = tokenUtils.getUserIdFromToken(jwtTokenProvider.resolveToken(request));
        User user = userRepository.findById(userId).orElseThrow();
        return getBaseUserResponseDto(user);
    }

    public void changePassword(String token, String rawPassword) {
        Long userId = tokenUtils.getUserIdFromToken(token);
        User user = userRepository.findById(userId).orElseThrow();
        String encryptedPw = new BCryptPasswordEncoder().encode(rawPassword);
        user.changePassword(encryptedPw);
    }

    public boolean checkEmail(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    public boolean checkUserLink(String userLink) {
        return userRepository.findByUserLink(userLink).isEmpty();
    }

    // Internal API
    public Long getUserIdByUserLink(String userLink) {
        return userRepository.findByUserLink(userLink).orElseThrow().getId();
    }

    public BaseUserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return getBaseUserResponseDto(user);
    }

    public void increasePostCnt(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        int currentCnt = user.getPostCnt() + 1;
        user.updateUser(new BaseUserUpdateRequestDto(null, null, null, null, null, null, currentCnt, null));
    }

    public BaseUserResponseDto getUserByUserLink(String userLink) {
        User user = userRepository.findByUserLink(userLink).orElseThrow();

        return getBaseUserResponseDto(user);
    }

    private BaseUserResponseDto getBaseUserResponseDto(User user) {
        List<CategoryResponseDto> categoryNames = user.getCategoryList().stream()
                .map(category -> new CategoryResponseDto(category.getId(), category.getCategoryName(), category.isExistSubCategory(), category.getCount(), subCategoryService.getSubCategoryList(category.getId())))
                .collect(Collectors.toList());

        List<Follow> followingList = user.getFollowingList().stream().toList();
        List<Follow> followerList = user.getFollowerList().stream().toList();

        List<FollowResponseDto> followingUsers = followingList.stream()
                .map(following -> {
                    User followingUser = following.getFollowingUser();
                    return new FollowResponseDto(
                            followingUser.getId(),
                            followingUser.getNickname(),
                            followingUser.getEmail(),
                            followingUser.getProfileUrl(),
                            followingUser.getUserLink());
                })
                .toList();

        List<FollowResponseDto> followerUsers = followerList.stream()
                .map(follower -> {
                    User followerUser = follower.getFollowerUser();
                    return new FollowResponseDto(
                            followerUser.getId(),
                            followerUser.getNickname(),
                            followerUser.getEmail(),
                            followerUser.getProfileUrl(),
                            followerUser.getUserLink());
                })
                .toList();

        return BaseUserResponseDto.of(user, categoryNames, followingUsers, followerUsers);
    }
}