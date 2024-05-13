package userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.dto.request.BaseUserRequestDto;
import userservice.dto.request.LoginRequestDto;
import userservice.dto.response.TokenResponseDto;
import userservice.service.AuthService;
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    @PostMapping("")
    public ResponseEntity<TokenResponseDto> register(@RequestBody BaseUserRequestDto baseUserRequestDto) {
        authService.register(baseUserRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest){
        authService.logout(httpServletRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(authService.reissue(httpServletRequest));
    }
}
