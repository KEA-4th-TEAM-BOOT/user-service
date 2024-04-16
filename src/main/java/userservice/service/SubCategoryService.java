package userservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import userservice.domain.Category;
import userservice.domain.SubCategory;
import userservice.repository.CategoryRepository;
import userservice.repository.SubCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@Service
public class SubCategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    public void createSubCategory(Long categoryId, String subCategoryName) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        SubCategory subCategory = SubCategory.createSubCategory(category, subCategoryName);
        subCategoryRepository.save(subCategory);
    }

    public List<String> getSubCategoryList(Long CategoryId) {
        Category category = categoryRepository.findById(CategoryId).orElseThrow();
        // 찾아온 카테고리에서 서브 카테고리 리스트를 추출하여 서브 카테고리 이름만 모아 리스트로 반환합니다.
        return category.getSubCategoryList().stream()
                .map(SubCategory::getSubCategoryName)
                .collect(Collectors.toList());
    }

    public void deleteSubCategory(Long subCategoryId) {
        subCategoryRepository.deleteById(subCategoryId);
    }

    public void updateSubCategory(Long subCategoryId, String subCategoryName) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElseThrow();
        subCategory.updateSubCategory(subCategoryName);
    }
}
