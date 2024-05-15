package userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.domain.Follow;
import userservice.service.FollowService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{userLink}")
    public ResponseEntity<Void> createFollow(@RequestHeader("Authorization")String token, @PathVariable String userLink){
        followService.createFollow(token, userLink);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/following")
    public ResponseEntity<List<String>> getFollowingList(HttpServletRequest httpServletRequest){
        List<String> followerList = followService.getFollowingList(httpServletRequest);
        return ResponseEntity.ok(followerList);
    }

    @GetMapping("/follower")
    public ResponseEntity<List<String>> getFollowerList(HttpServletRequest httpServletRequest){
        List<String> followedList = followService.getFollowedList(httpServletRequest);
        return ResponseEntity.ok(followedList);
    }

    @DeleteMapping("/{userLink}")
    public ResponseEntity<Void> deleteFollow(@RequestHeader("Authroization")String token, @PathVariable String userLink){

        followService.deleteFollow(token, userLink);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
