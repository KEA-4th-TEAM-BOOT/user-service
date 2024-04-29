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

    @GetMapping("/follow/{user_id}")
    public ResponseEntity<List<String>> getFollowerList(@PathVariable Long user_id){
//        List<String> followerList, followedList = followService.getFollowList(user_id);
        List<String> followerList = followService.getFollowList(user_id);
        return ResponseEntity.ok(followerList);
    }


}
