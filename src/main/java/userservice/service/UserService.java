package userservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;


    public TokenResponseDto register(BaseUserRequestDto baseUserRequestDto) {

        if(userRepository.findByEmail(baseUserRequestDto.email()) != null){
            return new TokenResponseDto(409, "이미 존재하는 이메일입니다", null, null);
        }
        String encryptedPw = passwordEncoder.encode(baseUserRequestDto.password());
        User user = User.createUser(baseUserRequestDto, encryptedPw);
        userRepository.save(user);
        return new TokenResponseDto(201, "회원가입 성공", null ,null);
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
        if(user == null)    //
            return new TokenResponseDto(417, "존재하지 않는 이메일입니다.", null, null);

        if(!passwordEncoder.matches(loginRequestDto.password(), user.getPassword()))
            return new TokenResponseDto(400, "비밀번호가 일치하지 않습니다.", null, null);

        String refreshToken = "Bearer " + jwtTokenProvider.createRefreshToken(user.getId());

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .code(200)
                .message("로그인 성공")
                .accessToken("Bearer " + jwtTokenProvider.createAccessToken(user.getId()))
                .refreshToken(refreshToken)
                .build();

        redisTemplate.opsForValue().set(String.valueOf(user.getId()), refreshToken);
        return tokenResponseDto;
    }

    public void logout(HttpServletRequest httpServletRequest){
        String accessToken = jwtTokenProvider.resolveToken(httpServletRequest);
//        jwtTokenProvider.validateRefreshToken(accessToken);
        String userId = jwtTokenProvider.getUserId(accessToken);
        log.info("[Token Info] 해당 억세스 토큰의 사용자 아이디: " + userId);
        redisTemplate.delete(userId);
        log.info("[Logout] 로그아웃 완료");
    }

    public TokenResponseDto reissue(HttpServletRequest httpServletRequest) {
        String accessToken = jwtTokenProvider.resolveToken(httpServletRequest);
        String refreshToken = httpServletRequest.getHeader("RefreshToken");
        String userId = jwtTokenProvider.getUserId(accessToken);

        if(!jwtTokenProvider.validateRefreshToken(refreshToken.substring(7))){
            return TokenResponseDto.builder()
                    .code(417)
                    .message("다시 로그인 해주세요.")
                    .build();
        }

        String redisRefreshToken = redisTemplate.opsForValue().get(userId);

        if(redisRefreshToken == null){
            return TokenResponseDto.builder()
                    .code(401)
                    .message("이미 로그아웃한 사용자압니다.")
                    .build();
        }

        if(redisRefreshToken.equals(refreshToken)){
            return TokenResponseDto.builder()
                    .code(200)
                    .message("토큰 재발급 완료")
                    .accessToken(jwtTokenProvider.createAccessToken(Long.valueOf(userId)))
                    .refreshToken(redisRefreshToken)
                    .build();
        }

        return TokenResponseDto.builder()
                .code(403)
                .message("올바르지 않는 접근입니다.")
                .build();
    }
}