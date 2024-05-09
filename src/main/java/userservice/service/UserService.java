package userservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import userservice.config.JwtTokenProvider;
import userservice.domain.User;
import userservice.dto.request.BaseUserUpdateRequestDto;
import userservice.dto.request.LoginRequestDto;
import userservice.dto.response.CategoryResponseDto;
import userservice.dto.request.BaseUserRequestDto;
import userservice.dto.response.BaseUserResponseDto;
import userservice.dto.response.TokenResponseDto;
import userservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;


    public void register(BaseUserRequestDto baseUserRequestDto) {
        String encryptedPw = passwordEncoder.encode(baseUserRequestDto.password());
        User user = User.createUser(baseUserRequestDto, encryptedPw);
        userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public void updateUser(Long id, BaseUserUpdateRequestDto baseUserUpdateRequestDto) {
        Optional<User> user = Optional.of(userRepository.findById(id).orElseThrow());
        user.get().updateUser(baseUserUpdateRequestDto);
    }

    public BaseUserResponseDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        List<CategoryResponseDto> categoryNames = user.getCategoryList().stream()
                .map(category -> new CategoryResponseDto(category.getCategoryName(), category.isExistSubCategory()))
                .collect(Collectors.toList());

        // DTO 생성 및 카테고리 이름 리스트 설정
        return BaseUserResponseDto.of(user, categoryNames);
    }

    public TokenResponseDto login(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByEmail(loginRequestDto.email());
        System.out.println(user);

        if(user == null)    //
            return new TokenResponseDto(417, "No Email", null, null);

        String refreshToken = "Bearer " + jwtTokenProvider.createRefreshToken(user.getId());

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .code(200)
                .accessToken("Bearer " + jwtTokenProvider.createAccessToken(user.getId()))
                .refreshToken(refreshToken)
                .build();

        redisTemplate.opsForValue().set(String.valueOf(user.getId()), refreshToken);

        return tokenResponseDto;
    }

    public void logout(HttpServletRequest httpServletRequest){
        String userId = httpServletRequest.getHeader("user");
        System.out.println(String.valueOf(userId));
        redisTemplate.delete(userId);
    }

    public void logout(String accessToken, String refreshToken) {
    }
}