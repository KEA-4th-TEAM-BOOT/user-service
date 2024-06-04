package userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "게시물 수 증가", description = "특정 사용자의 게시물 수를 증가시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시물 수 증가 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("/increasePostCnt/{userId}")
    public ResponseEntity<Void> increasePostCnt(@PathVariable Long userId){
        userService.increasePostCnt(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "현재 사용자 정보 조회", description = "토큰을 통해 현재 사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    // 이거 토큰 DTO로 보내지말고 Login DTO나 다른거로 보내야 되는거 확실한데 잘 모르겠다
    @GetMapping("")
    public ResponseEntity<BaseUserResponseDto> getUser(HttpServletRequest request) {
        BaseUserResponseDto userResponseDto = userService.getUser(request);
        return ResponseEntity.ok(userResponseDto);
    }

    @Operation(summary = "사용자 ID로 정보 조회", description = "사용자 ID를 통해 사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<BaseUserResponseDto> getUserById(@PathVariable Long userId){
        BaseUserResponseDto userResponseDto = userService.getUserById(userId);
        return ResponseEntity.ok(userResponseDto);
    }

    @Operation(summary = "사용자 링크로 정보 조회", description = "사용자 링크를 통해 사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/other/{userLink}")
    public ResponseEntity<BaseUserResponseDto> getUserByUserLink(@PathVariable String userLink){
        BaseUserResponseDto userResponseDto = userService.getUserByUserLink(userLink);
        return ResponseEntity.ok(userResponseDto);
    }

    @Operation(summary = "내부용 사용자 링크로 사용자 ID 조회", description = "내부 처리를 위해 사용자 링크로 사용자 ID를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 ID 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/internal/{userLink}")
    public Long getUserIdByUserLink(@PathVariable String userLink) {
        return userService.getUserIdByUserLink(userLink);
    }

    @Operation(summary = "사용자 삭제", description = "현재 사용자를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "사용자 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @DeleteMapping("")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request) {
        userService.deleteUser(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "사용자 정보 수정", description = "현재 사용자의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PatchMapping("")
    public ResponseEntity<Void> updateUser(@RequestHeader("Authorization") String token, @RequestBody BaseUserUpdateRequestDto baseUserUpdateRequestDto) {
        userService.updateUser(token, baseUserUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
//        String userId = (String) request.getAttribute("userId");

    @Operation(summary = "비밀번호 변경", description = "현재 사용자의 비밀번호를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PatchMapping("resetPw")
    public ResponseEntity<Void> changePassword(@RequestHeader("Authorization") String token, @RequestBody String rawPassword) {
        userService.changePassword(token, rawPassword);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
