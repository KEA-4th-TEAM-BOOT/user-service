package userservice.utils;

import org.springframework.stereotype.Component;
import userservice.config.JwtTokenProvider;

@Component
public class TokenUtils {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenUtils(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // 'Bearer '를 제거합니다.
        }
        if (token != null) {
            return Long.valueOf(jwtTokenProvider.getUserId(token));
        }
        return null;  // token이 null인 경우 null을 반환합니다.
    }

}
