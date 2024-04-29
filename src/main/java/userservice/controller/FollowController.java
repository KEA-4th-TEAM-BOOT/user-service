package userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.domain.Follow;
import userservice.service.FollowService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowService followService;
    @PostMapping("/follow/{follower_id}")
    public ResponseEntity<Void> createFollow(@PathVariable Long follower_id, @RequestBody Long followed_id){
        followService.createFollow(follower_id, followed_id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/follower/{follower_id}")
    public ResponseEntity<List<String>> getFollowerList(@PathVariable Long follower_id){
        List<String> followerList = followService.getFollowerList(follower_id);
        return ResponseEntity.ok(followerList);
    }

    @GetMapping("/followed/{followed_id}")
    public ResponseEntity<List<String>> getFollowedList(@PathVariable Long followed_id){
        List<String> followedList = followService.getFollowedList(followed_id);
        return ResponseEntity.ok(followedList);
    }

    @DeleteMapping("/follow/{follower_id}")
    public ResponseEntity<Void> deleteFollow(@PathVariable Long follower_id, @RequestBody Long followed_id){
        followService.deleteFollow(follower_id, followed_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
