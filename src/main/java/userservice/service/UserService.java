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
import userservice.dto.request.BaseUserRequestDto;
import userservice.dto.request.BaseUserUpdateRequestDto;
import userservice.dto.response.CategoryResponseDto;
import userservice.dto.response.BaseUserResponseDto;
import userservice.dto.response.FollowResponseDto;
import userservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;
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

    public void deleteUser(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));
        userRepository.deleteById(userId);
    }

    public void updateUser(String token, BaseUserUpdateRequestDto baseUserUpdateRequestDto) {
        String accessToken = token.substring(7);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));
        Optional<User> user = Optional.of(userRepository.findById(userId).orElseThrow());
        user.get().updateUser(baseUserUpdateRequestDto);
    }

    public BaseUserResponseDto getUser(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));
        User user = userRepository.findById(userId).orElseThrow();
        List<CategoryResponseDto> categoryNames = user.getCategoryList().stream()
                .map(category -> new CategoryResponseDto(category.getId(), category.getCategoryName(), category.isExistSubCategory(), category.getCount(), subCategoryService.getSubCategoryList(category.getId())))
                .collect(Collectors.toList());

        List<Follow> followingList = user.getFollowingList().stream().toList();
        List<Follow> followerList = user.getFollowerList().stream().toList();

        List<FollowResponseDto> followingUsers = followingList.stream()
                .map(following -> {
                    User followingUser = following.getFollowingUser();
                    return new FollowResponseDto(
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
                            followerUser.getNickname(),
                            followerUser.getEmail(),
                            followerUser.getProfileUrl(),
                            followerUser.getUserLink());
                })
                .toList();

        // DTO 생성 및 카테고리 이름 리스트 설정
        return BaseUserResponseDto.of(user, categoryNames, followingUsers, followerUsers);
    }

    public void changePassword(String token, String rawPassword) {
        String accessToken = token.substring(7);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));
        User user = userRepository.findById(userId).orElseThrow();
        String encryptedPw = new BCryptPasswordEncoder().encode(rawPassword);
        user.changePassword(encryptedPw);
    }

    public Boolean checkEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user == null;
    }

    public Boolean checkUserLink(String userLink) {
        User user = userRepository.findByUserLink(userLink);
        return user == null;
    }

    // Internal API
    public Long getUserByUserLink(String userLink) {
        return userRepository.findByUserLink(userLink).getId();
    }

    public BaseUserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId).get();

        List<CategoryResponseDto> categoryNames = user.getCategoryList().stream()
                .map(category -> new CategoryResponseDto(category.getId(), category.getCategoryName(), category.isExistSubCategory(), category.getCount(), subCategoryService.getSubCategoryList(category.getId())))
                .collect(Collectors.toList());

        List<Follow> followingList = user.getFollowingList().stream().toList();
        List<Follow> followerList = user.getFollowerList().stream().toList();

        List<FollowResponseDto> followingUsers = followingList.stream()
                .map(following -> {
                    User followingUser = following.getFollowingUser();
                    return new FollowResponseDto(
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
                            followerUser.getNickname(),
                            followerUser.getEmail(),
                            followerUser.getProfileUrl(),
                            followerUser.getUserLink());
                })
                .toList();

        return BaseUserResponseDto.of(user, categoryNames, followingUsers, followerUsers);
    }
}