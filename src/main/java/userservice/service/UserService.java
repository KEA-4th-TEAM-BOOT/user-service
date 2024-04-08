package userservice.service;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import userservice.domain.User;
import userservice.repository.UserRepository;
import userservice.vo.BaseUserEnumVo;

@Transactional
@Service
public class UserService {

    private static UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        UserService.userRepository = userRepository;
    }

    public static ResponseEntity<User> createUser(BaseUserEnumVo baseUserEnumVo) {
        User user = User.createUser(baseUserEnumVo);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    public String hello(){
        return "hello";
    }
}
