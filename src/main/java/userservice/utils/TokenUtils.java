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
        return Long.valueOf(jwtTokenProvider.getUserId(token));
    }

}
