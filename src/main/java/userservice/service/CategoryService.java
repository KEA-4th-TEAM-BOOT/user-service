package userservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import userservice.config.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final SubCategoryService subCategoryService;

    public void createCategory(String token, String categoryName) {
        String accessToken = token.substring(7);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Category category = Category.createCategory(user, categoryName);
        categoryRepository.save(category);
    }

    public List<CategoryResponseDto> getCategoryList(HttpServletRequest httpServletRequest) {
        String accessToken = jwtTokenProvider.resolveToken(httpServletRequest);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Stream을 사용하여 각 카테고리의 이름을 추출하고 리스트로 수집
        return user.getCategoryList().stream()
                .map(category -> new CategoryResponseDto(category.getId(), category.getCategoryName(), category.isExistSubCategory(), category.getCount(), subCategoryService.getSubCategoryList(category.getId())))
                .toList();
    }

    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public void updateCategory(Long categoryId, BaseCategoryEnumVo baseCategoryEnumVo) {
        Optional<Category> category = Optional.of(categoryRepository.findById(categoryId).orElseThrow());
        category.get().updateCategory(baseCategoryEnumVo);
    }

    public void increasePostCount(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        category.addPostCount();
    }
}