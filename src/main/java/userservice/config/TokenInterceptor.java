//package userservice.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.HandlerInterceptor;
//import userservice.dto.response.TokenResponseDto;
//
//import java.util.Arrays;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Configuration
//public class TokenInterceptor implements HandlerInterceptor {
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        // 기본 경로
//        String basePath = "/api/v1";
//        // 인터셉터를 건너뛸 하위 경로 목록
//        List<String> pathsToSkip = Arrays.asList("/swagger-ui", "/api-docs", "/login", "/register");
//
//        String requestURI = request.getRequestURI();
//        // 요청 URI가 해당하는 하위 경로를 어디에든 포함하는지 검사
//        boolean shouldSkip = pathsToSkip.stream().anyMatch(requestURI::contains);
//
//        // shouldSkip이 false인 경우, 즉 건너뛰지 않아야 할 경우 로그인 페이지로 리다이렉트
//        if (!shouldSkip) {
//            response.sendRedirect(basePath + "/login");
//        }
//
//        String token = jwtTokenProvider.resolveToken(request);
//
//        if (token != null) {
//            String userId = jwtTokenProvider.getUserId(token);
//            request.setAttribute("userId", userId);
//            return true;
//        } else {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return false;
//        }
//    }
//}
