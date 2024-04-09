package userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.domain.User;
import userservice.dto.request.BaseUserRequestDto;
import userservice.repository.UserRepository;
import userservice.service.UserService;
import userservice.vo.BaseUserEnumVo;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String hello(){
        return userService.hello();
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody BaseUserRequestDto baseUserRequestDto) {
        return UserService.createUser(baseUserRequestDto);
    }
}
