package userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.dto.request.BaseUserRequestDto;
import userservice.dto.request.EmailAuthRequestDto;
import userservice.dto.request.LoginRequestDto;
import userservice.dto.response.TokenResponseDto;
import userservice.service.AuthService;
import userservice.service.MailService;
import userservice.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final MailService mailService;
    private final UserService userService;

    @PostMapping("register")
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

    @PostMapping("send-email")
    public String sendEmail(@RequestBody EmailAuthRequestDto emailAuthRequestDto){
        log.info("[Email] 인증할 이메일 주소: " + emailAuthRequestDto.email());
        return mailService.sendEmail(emailAuthRequestDto.email());
    }

    @PostMapping("/verification")
    public String AuthNumCheck(@RequestBody EmailAuthRequestDto EmailAuthRequestDto){
        log.info("[Email] 인증번호 검증");
        Boolean authCheck = mailService.checkAuth(EmailAuthRequestDto);
        if(authCheck)
            return "ok";
        else
            return "유효하지 않은 인증번호입니다.";
    }

    @GetMapping("/check/{email}")
    public ResponseEntity<Void> emailCheck(@PathVariable String email){
        Boolean isEmailDuplicate = userService.checkEmail(email);
        if(isEmailDuplicate){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/check/{userLink}")
    public ResponseEntity<Void> userLinkCheck(@PathVariable String userLink){
        Boolean isUserLinkDuplicate = userService.checkUserLink(userLink);
        if(isUserLinkDuplicate){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
