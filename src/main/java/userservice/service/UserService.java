package userservice.service;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import userservice.domain.User;
import userservice.dto.request.BaseUserRequestDto;
import userservice.repository.UserRepository;
import userservice.vo.BaseUserEnumVo;

import java.util.Optional;

@Transactional
@Service
public class UserService {

    private static UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        UserService.userRepository = userRepository;
    }

    public void createUser(BaseUserRequestDto baseUserRequestDto) {
        User user = User.createUser(baseUserRequestDto);
        userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
        ResponseEntity.ok(id);
    }

    public void updateUser(Long id, BaseUserEnumVo baseUserEnumVo) {
        Optional<User> user = Optional.of(userRepository.findById(id).orElseThrow());
        user.get().updateUser(baseUserEnumVo);
    }

    public Optional<User> getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user;
    }
}
