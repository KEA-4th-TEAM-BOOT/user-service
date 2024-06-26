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
import userservice.utils.TokenUtils;
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
    private final TokenUtils tokenUtils;

    public void createCategory(String token, String categoryName) {
        Long userId = tokenUtils.getUserIdFromToken(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Category category = Category.createCategory(user, categoryName);
        categoryRepository.save(category);
    }

    public List<CategoryResponseDto> getCategoryList(HttpServletRequest httpServletRequest) {
        Long userId = tokenUtils.getUserIdFromToken(jwtTokenProvider.resolveToken(httpServletRequest));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

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

    public List<CategoryResponseDto> getUserCategoryList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Stream을 사용하여 각 카테고리의 이름을 추출하고 리스트로 수집
        return user.getCategoryList().stream()
                .map(category -> new CategoryResponseDto(category.getId(), category.getCategoryName(), category.isExistSubCategory(), category.getCount(), subCategoryService.getSubCategoryList(category.getId())))
                .toList();
    }

    public void createCategoryUsingUserId(Long userId, String categoryName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Category category = Category.createCategory(user, categoryName);
        categoryRepository.save(category);
    }
}