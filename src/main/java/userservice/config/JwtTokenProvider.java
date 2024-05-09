package userservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Signature;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String base64Secret;
    private Key secretKey;
    private final long accessTokenTime = 24L * 60 * 60 * 1000; // 하루 토큰 유효
    private final long refreshTokenTime = 30L * 24L * 60 * 60 * 1000; // 1달 토큰 유효

    @PostConstruct
    protected void init(){
        byte[] decodekey = Base64.getDecoder().decode(base64Secret);
        secretKey = new SecretKeySpec(decodekey, SignatureAlgorithm.HS256.getJcaName());
        logger.info("[Initialize Secret key]");
    }

    public String createAccessToken(Long userId) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        logger.info("[createToken] 로컬 엑세스 토큰 생성 완료");
        return token;
    }

    public String createRefreshToken(Long userId) {
        Date now = new Date();
        String token = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        logger.info("[createToken] 로컬 리프레쉬 토큰 생성 완료");
        return token;
    }

    public String resolveToken(HttpServletRequest request) {
        logger.info("[resolveToken] HTTP 헤더에서 Token 값 추출");

        String tokenHeader = request.getHeader("Authorization");

        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            return tokenHeader.substring(7);
        } else {
            // 예외 처리: 헤더가 없거나 "Bearer " 접두사가 없는 경우
            throw new IllegalArgumentException("Invalid access token header");
        }
    }

    public boolean validateRefreshToken(String token){
        logger.info("[validateRefreshToken] 토큰 유효 체크 시작");
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
        if(!claimsJws.getBody().isEmpty()){
            logger.info("[validateRefreshToken] 토큰 유효 체크 완료");
            return true;
        }
        return false;
    }

    public String getUserId(String accessToken){
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(accessToken);
        logger.info("[getUserIdFromAccessToken] UserId 추출");
        return claimsJws.getBody().getSubject();
    }
}
