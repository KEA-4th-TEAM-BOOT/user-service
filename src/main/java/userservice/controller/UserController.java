package userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.domain.User;
import userservice.dto.request.BaseUserRequestDto;
import userservice.dto.response.BaseUserResponseDto;
import userservice.service.UserService;
import userservice.vo.BaseUserEnumVo;

import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<Void> createUser(@RequestBody BaseUserRequestDto baseUserRequestDto) {
        userService.createUser(baseUserRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<BaseUserResponseDto> getUser(@PathVariable Long id){
        BaseUserResponseDto userResponseDto = userService.getUser(id);
        return ResponseEntity.ok(userResponseDto);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody BaseUserEnumVo baseUserEnumVo){
        userService.updateUser(id, baseUserEnumVo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
