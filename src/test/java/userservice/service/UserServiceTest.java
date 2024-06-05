package userservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;
import userservice.config.JwtTokenProvider;
import userservice.domain.User;
import userservice.dto.request.BaseUserUpdateRequestDto;
import userservice.dto.response.BaseUserResponseDto;
import userservice.repository.UserRepository;
import userservice.utils.TokenUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private TokenUtils tokenUtils;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(jwtTokenProvider.resolveToken(request)).thenReturn("someToken");
        when(tokenUtils.getUserIdFromToken("someToken")).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.deleteUser(request));
    }

//    @Test
//    public void testDeleteUser_Success() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        // HttpServletRequest의 getHeader("Authorization") 메서드가 호출될 때 "Bearer someToken"을 반환하도록 설정
//        when(request.getHeader("Authorization")).thenReturn("Bearer someToken");
//
//        // jwtTokenProvider의 resolveToken(request) 메서드가 호출될 때 "someToken"을 반환하도록 설정
//        when(jwtTokenProvider.resolveToken(request)).thenReturn("someToken");
//
//        // jwtTokenProvider의 getUserId("someToken") 메서드가 호출될 때 "1"을 반환하도록 설정
//        when(jwtTokenProvider.getUserId("someToken")).thenReturn("1");
//
//        // tokenUtils의 getUserIdFromToken("someToken") 메서드가 호출될 때 1L을 반환하도록 설정
//        when(tokenUtils.getUserIdFromToken("someToken")).thenReturn(1L);
//
//        User user = User.builder().id(1L).build();
//        // userRepository의 findById(1L) 메서드가 호출될 때 Optional.of(user)를 반환하도록 설정
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        // 로그 추가
//        System.out.println("Authorization Header: " + request.getHeader("Authorization"));
//        System.out.println("Resolved Token: " + jwtTokenProvider.resolveToken(request));
//
//
//        // 실제 서비스 메서드 호출
//        userService.deleteUser(request);
//
//        // userRepository의 deleteById(1L) 메서드가 한 번 호출되었는지 검증
//        verify(userRepository, times(1)).deleteById(1L);
//    }

    @Test
    public void testUpdateUser_UserNotFound() {
        String token = "someToken";
        BaseUserUpdateRequestDto updateRequestDto = new BaseUserUpdateRequestDto("newNickname", "newProfileUrl", "newIntroduce", 0, 0, 0, 0, "newVoiceModelUrl");
        when(tokenUtils.getUserIdFromToken(token)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(token, updateRequestDto));
    }

//    @Test
//    public void testUpdateUser_Success() {
//        String token = "someToken";
//        BaseUserUpdateRequestDto updateRequestDto = new BaseUserUpdateRequestDto("newNickname", "newProfileUrl", "newIntroduce", 0, 0, 0, 0, "newVoiceModelUrl");
//        when(tokenUtils.getUserIdFromToken(token)).thenReturn(1L);
//        User user = User.builder().id(1L).build();
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        userService.updateUser(token, updateRequestDto);
//
//        verify(userRepository, times(1)).save(user);
//    }

    @Test
    public void testGetUser_UserNotFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(jwtTokenProvider.resolveToken(request)).thenReturn("someToken");
        when(tokenUtils.getUserIdFromToken("someToken")).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUser(request));
    }

//    @Test
//    public void testGetUser_Success() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtTokenProvider.resolveToken(request)).thenReturn("someToken");
//        when(tokenUtils.getUserIdFromToken("someToken")).thenReturn(1L);
//        User user = User.builder().id(1L).build();
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        BaseUserResponseDto response = userService.getUser(request);
//
//        assertNotNull(response);
//    }

    @Test
    public void testChangePassword_UserNotFound() {
        String token = "someToken";
        String newPassword = "newPassword";
        when(tokenUtils.getUserIdFromToken(token)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.changePassword(token, newPassword));
    }

//    @Test
//    public void testChangePassword_Success() {
//        String token = "someToken";
//        String newPassword = "newPassword";
//        when(tokenUtils.getUserIdFromToken(token)).thenReturn(1L);
//        User user = User.builder().id(1L).build();
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        userService.changePassword(token, newPassword);
//
//        verify(userRepository, times(1)).save(user);
//    }

    @Test
    public void testCheckEmail() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        boolean result = userService.checkEmail(email);

        assertTrue(result);
    }

    @Test
    public void testCheckUserLink() {
        String userLink = "testUserLink";
        when(userRepository.findByUserLink(userLink)).thenReturn(Optional.empty());

        boolean result = userService.checkUserLink(userLink);

        assertTrue(result);
    }
}
