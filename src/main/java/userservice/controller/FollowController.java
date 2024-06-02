package userservice.controller;

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

    @PostMapping("/{userLink}")
    public ResponseEntity<Void> createFollow(@RequestHeader("Authorization") String token, @PathVariable String userLink) {
        followService.createFollow(token, userLink);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/following")
    public ResponseEntity<List<FollowResponseDto>> getFollowingList(HttpServletRequest httpServletRequest) {
        List<FollowResponseDto> followerList = followService.getFollowingList(httpServletRequest);
        return ResponseEntity.ok(followerList);
    }

    @GetMapping("/follower")
    public ResponseEntity<List<FollowResponseDto>> getFollowerList(HttpServletRequest httpServletRequest) {
        List<FollowResponseDto> followedList = followService.getFollowedList(httpServletRequest);
        return ResponseEntity.ok(followedList);
    }

    @DeleteMapping("/{userLink}")
    public ResponseEntity<Void> deleteFollow(@RequestHeader("Authroization") String token, @PathVariable String userLink) {
        followService.deleteFollow(token, userLink);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
