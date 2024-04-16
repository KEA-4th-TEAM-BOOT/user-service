package userservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import userservice.domain.User;
import userservice.dto.response.CategoryResponseDto;
import userservice.dto.request.BaseUserRequestDto;
import userservice.dto.response.BaseUserResponseDto;
import userservice.repository.UserRepository;
import userservice.vo.BaseUserEnumVo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;


    public void createUser(BaseUserRequestDto baseUserRequestDto) {
        User user = User.createUser(baseUserRequestDto);
        userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public void updateUser(Long id, BaseUserEnumVo baseUserEnumVo) {
        Optional<User> user = Optional.of(userRepository.findById(id).orElseThrow());
        user.get().updateUser(baseUserEnumVo);
    }

    public BaseUserResponseDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        List<CategoryResponseDto> categoryNames = user.getCategoryList().stream()
                .map(category -> new CategoryResponseDto(category.getCategoryName(), category.isExistSubCategory()))
                .collect(Collectors.toList());

        // DTO 생성 및 카테고리 이름 리스트 설정
        return BaseUserResponseDto.of(user, categoryNames);
    }
}