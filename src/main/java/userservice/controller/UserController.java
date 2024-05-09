package userservice.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.domain.User;
import userservice.dto.request.BaseUserRequestDto;
import userservice.dto.request.BaseUserUpdateRequestDto;
import userservice.dto.request.LoginRequestDto;
import userservice.dto.response.BaseUserResponseDto;
import userservice.dto.response.TokenResponseDto;
import userservice.service.UserService;
import userservice.vo.BaseUserEnumVo;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<Void> register(@RequestBody BaseUserRequestDto baseUserRequestDto) {
        userService.register(baseUserRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseUserResponseDto> getUser(@PathVariable Long id){
        BaseUserResponseDto userResponseDto = userService.getUser(id);
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest){
        String userId = httpServletRequest.getHeader("user");
        System.out.println("버근가");
        log.info("버근가2");
//        userService.logout(accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody BaseUserUpdateRequestDto baseUserUpdateRequestDto){
        userService.updateUser(id, baseUserUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody LoginRequestDto loginRequestDto){
        return userService.login(loginRequestDto);
    }
}
