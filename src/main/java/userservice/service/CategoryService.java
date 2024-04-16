package userservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import userservice.domain.Category;
import userservice.domain.User;
import userservice.dto.response.CategoryResponseDto;
import userservice.repository.CategoryRepository;
import userservice.repository.UserRepository;
import userservice.vo.BaseCategoryEnumVo;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public void createCategory(Long id, String categoryName) {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        Category category = Category.createCategory(user, categoryName);
        categoryRepository.save(category);
    }

    public List<CategoryResponseDto> getCategoryList(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        // Stream을 사용하여 각 카테고리의 이름을 추출하고 리스트로 수집

        return user.getCategoryList().stream()
                .map(category -> new CategoryResponseDto(category.getCategoryName(), category.isExistSubCategory()))
                .toList();
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public void updateCategory(Long id, BaseCategoryEnumVo baseCategoryEnumVo) {
        Optional<Category> category = Optional.of(categoryRepository.findById(id).orElseThrow());
        category.get().updateCategory(baseCategoryEnumVo);
    }
}