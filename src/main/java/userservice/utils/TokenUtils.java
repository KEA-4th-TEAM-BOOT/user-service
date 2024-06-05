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
        String accessToken = extractAccessToken(token);
        return Long.valueOf(jwtTokenProvider.getUserId(accessToken));
    }

    public String extractAccessToken(String token) {
        return token.substring(7); // Assuming "Bearer " prefix
    }
}
