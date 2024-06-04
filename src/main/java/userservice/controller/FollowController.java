package userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.domain.Follow;
import userservice.dto.response.FollowResponseDto;
import userservice.service.FollowService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "팔로우 생성", description = "사용자가 다른 사용자를 팔로우합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "팔로우 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/{userLink}")
    public ResponseEntity<Void> createFollow(@RequestHeader("Authorization") String token, @PathVariable String userLink) {
        followService.createFollow(token, userLink);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "팔로잉 목록 조회", description = "사용자가 팔로우하고 있는 사용자 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로잉 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/following")
    public ResponseEntity<List<FollowResponseDto>> getFollowingList(HttpServletRequest httpServletRequest) {
        List<FollowResponseDto> followerList = followService.getFollowingList(httpServletRequest);
        return ResponseEntity.ok(followerList);
    }

    @Operation(summary = "팔로워 목록 조회", description = "사용자를 팔로우하고 있는 사용자 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로워 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/follower")
    public ResponseEntity<List<FollowResponseDto>> getFollowerList(HttpServletRequest httpServletRequest) {
        List<FollowResponseDto> followedList = followService.getFollowedList(httpServletRequest);
        return ResponseEntity.ok(followedList);
    }

    @Operation(summary = "팔로우 삭제", description = "사용자가 다른 사용자에 대한 팔로우를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "팔로우 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "팔로우를 찾을 수 없음")
    })
    @DeleteMapping("/{userLink}")
    public ResponseEntity<Void> deleteFollow(@RequestHeader("Authroization") String token, @PathVariable String userLink) {
        followService.deleteFollow(token, userLink);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
