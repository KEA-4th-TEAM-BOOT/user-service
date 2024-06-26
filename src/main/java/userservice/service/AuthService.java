package userservice.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
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
import userservice.dto.response.LoginResponseDto;
import userservice.dto.response.TokenResponseDto;
import userservice.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final CategoryService categoryService;
    private final MeterRegistry meterRegistry;

    private Counter registerCounter;
    private Counter loginSuccessCounter;
    private Counter loginFailureCounter;

    @PostConstruct
    public void init() {
        this.registerCounter = meterRegistry.counter("user_register_count");
        this.loginSuccessCounter = meterRegistry.counter("user_login_success_count");
        this.loginFailureCounter = meterRegistry.counter("user_login_failure_count");
    }

    public User register(BaseUserRequestDto baseUserRequestDto) {
        String encryptedPw = passwordEncoder.encode(baseUserRequestDto.password());
        User user = User.createUser(baseUserRequestDto, encryptedPw);
        userRepository.save(user);
        registerCounter.increment(); // 회원가입 시 메트릭 증가
        return user;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
      
        Optional<User> user = userRepository.findByEmail(loginRequestDto.email());
        if (user.isEmpty()) {
            loginFailureCounter.increment(); // 로그인 실패 시 메트릭 증가
          
            return LoginResponseDto.builder()
                    .tokenResponseDto(new TokenResponseDto(417, "존재하지 않는 이메일입니다.", null, null))
                    .build();
        }

        log.info(String.valueOf(passwordEncoder.matches(loginRequestDto.password(), user.get().getPassword())));
      
        if (!passwordEncoder.matches(loginRequestDto.password(), user.get().getPassword())) {
            loginFailureCounter.increment(); // 로그인 실패 시 메트릭 증가
            return LoginResponseDto.builder()
                    .tokenResponseDto(new TokenResponseDto(400, "비밀번호가 일치하지 않습니다.", null, null))
                    .build();
        }

        String refreshToken = "Bearer " + jwtTokenProvider.createRefreshToken(user.get().getId());

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .tokenResponseDto(TokenResponseDto.builder()
                        .code(200)
                        .message("로그인 성공")
                        .accessToken("Bearer " + jwtTokenProvider.createAccessToken(user.get().getId()))
                        .refreshToken(refreshToken)
                        .build()
                )
                .userId(user.get().getId())
                .userLink(user.get().getUserLink())
                .profileUrl(user.get().getProfileUrl())
                .build();

        redisTemplate.opsForValue().set(String.valueOf(user.get().getId()), refreshToken);
        loginSuccessCounter.increment(); // 로그인 성공 시 메트릭 증가
        return loginResponseDto;
    }

    public void logout(HttpServletRequest httpServletRequest) {
        String accessToken = jwtTokenProvider.resolveToken(httpServletRequest);
        String userId = jwtTokenProvider.getUserId(accessToken);
        log.info("[Token Info] 해당 억세스 토큰의 사용자 아이디: {}", userId);
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
                    .message("이미 로그아웃한 사용자입니다.")
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
