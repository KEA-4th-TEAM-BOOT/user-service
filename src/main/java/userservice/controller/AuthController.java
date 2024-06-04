package userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.dto.request.BaseUserRequestDto;
import userservice.dto.request.EmailAuthRequestDto;
import userservice.dto.request.LoginRequestDto;
import userservice.dto.response.LoginResponseDto;
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

    @Operation(summary = "회원가입", description = "사용자가 회원가입을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("register")
    public ResponseEntity<Void> register(@RequestBody BaseUserRequestDto baseUserRequestDto) {
        authService.register(baseUserRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "로그인", description = "사용자가 로그인 요청을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        if(loginResponseDto.userLink() == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(loginResponseDto);
    }

    @Operation(summary = "로그아웃", description = "사용자가 로그아웃 요청을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "로그아웃 성공")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest) {
        authService.logout(httpServletRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "토큰 재발급", description = "사용자가 토큰 재발급 요청을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공")
    })
    @PatchMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(authService.reissue(httpServletRequest));
    }

    @Operation(summary = "이메일 인증 요청", description = "사용자가 이메일 인증을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 요청 성공")
    })
    @PostMapping("send-email")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailAuthRequestDto emailAuthRequestDto) {
        log.info("[Email] 인증할 이메일 주소: " + emailAuthRequestDto.email());
        mailService.sendEmail(emailAuthRequestDto.email());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이메일 인증번호 검증", description = "사용자가 받은 인증번호를 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 검증 성공"),
            @ApiResponse(responseCode = "204", description = "인증번호 검증 실패")
    })
    @PostMapping("/verification")
    public ResponseEntity<Void> AuthNumCheck(@RequestBody EmailAuthRequestDto EmailAuthRequestDto) {
        log.info("[Email] 인증번호 검증");
        Boolean authCheck = mailService.checkAuth(EmailAuthRequestDto);
        if (authCheck)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.noContent().build(); // bad request 보내기
    }

    @Operation(summary = "이메일 중복 체크", description = "사용자가 입력한 이메일의 중복 여부를 체크합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일이 중복되지 않음"),
            @ApiResponse(responseCode = "400", description = "이메일이 중복됨")
    })
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Void> emailCheck(@PathVariable String email) {
        Boolean isEmailDuplicate = userService.checkEmail(email);
        if (isEmailDuplicate) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "사용자 링크 중복 체크", description = "사용자가 입력한 링크의 중복 여부를 체크합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "링크가 중복되지 않음"),
            @ApiResponse(responseCode = "400", description = "링크가 중복됨")
    })
    @GetMapping("/check-userLink/{userLink}")
    public ResponseEntity<String> userLinkCheck(@PathVariable String userLink) {
        Boolean isUserLinkDuplicate = userService.checkUserLink(userLink);
        if (isUserLinkDuplicate) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
