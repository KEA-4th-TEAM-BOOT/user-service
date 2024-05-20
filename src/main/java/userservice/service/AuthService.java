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
import userservice.dto.request.BaseUserRequestDto;
import userservice.dto.request.LoginRequestDto;
import userservice.dto.response.TokenResponseDto;
import userservice.repository.UserRepository;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public void register(BaseUserRequestDto baseUserRequestDto) {

//        String encryptedPw = passwordEncoder.encode(baseUserRequestDto.password());
        User user = User.createUser(baseUserRequestDto);
        userRepository.save(user);
    }


    public TokenResponseDto login(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByEmail(loginRequestDto.email());
        if (user == null)    //
            return new TokenResponseDto(417, "존재하지 않는 이메일입니다.", null, null);

        if (!passwordEncoder.matches(loginRequestDto.password(), user.getPassword()))
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

    public void logout(HttpServletRequest httpServletRequest) {
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

        if (!jwtTokenProvider.validateRefreshToken(refreshToken.substring(7))) {
            return TokenResponseDto.builder()
                    .code(417)
                    .message("다시 로그인 해주세요.")
                    .build();
        }

        String redisRefreshToken = redisTemplate.opsForValue().get(userId);

        if (redisRefreshToken == null) {
            return TokenResponseDto.builder()
                    .code(401)
                    .message("이미 로그아웃한 사용자압니다.")
                    .build();
        }

        if (redisRefreshToken.equals(refreshToken)) {
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
