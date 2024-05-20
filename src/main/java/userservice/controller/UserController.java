package userservice.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    // 이거 토큰 DTO로 보내지말고 Login DTO나 다른거로 보내야 되는거 확실한데 잘 모르겠다
    @GetMapping("")
    public ResponseEntity<BaseUserResponseDto> getUser(HttpServletRequest request) {
        BaseUserResponseDto userResponseDto = userService.getUser(request);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/internal/{userLink}")
    public Long getUserByUserLink(@PathVariable String userLink) {
        return userService.getUserByUserLink(userLink);
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request) {
        userService.deleteUser(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("")
    public ResponseEntity<Void> updateUser(@RequestHeader("Authorization") String token, @RequestBody BaseUserUpdateRequestDto baseUserUpdateRequestDto) {
        userService.updateUser(token, baseUserUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
//        String userId = (String) request.getAttribute("userId");

    @PatchMapping("resetPw")
    public ResponseEntity<Void> changePassword(@RequestHeader("Authorization") String token, @RequestBody String rawPassword) {
        userService.changePassword(token, rawPassword);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
