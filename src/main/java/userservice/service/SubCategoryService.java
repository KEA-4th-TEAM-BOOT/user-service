package userservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import userservice.domain.Category;
import userservice.domain.SubCategory;
import userservice.dto.response.SubCategoryResponseDto;
import userservice.repository.CategoryRepository;
import userservice.repository.SubCategoryRepository;
import userservice.vo.BaseCategoryEnumVo;

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

    public List<SubCategoryResponseDto> getSubCategoryList(Long CategoryId) {
        Category category = categoryRepository.findById(CategoryId).orElseThrow();
        // 찾아온 카테고리에서 서브 카테고리 리스트를 추출하여 서브 카테고리 이름만 모아 리스트로 반환합니다.
        return category.getSubCategoryList().stream()
                .map(subCategory -> new SubCategoryResponseDto(
                        subCategory.getId(),
                        subCategory.getSubCategoryName(),
                        subCategory.getCount()
                ))
                .collect(Collectors.toList());
    }

    public void deleteSubCategory(Long subCategoryId) {
        // 서브카테고리를 찾아서 연관된 카테고리 ID를 얻습니다.
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        Long categoryId = subCategory.getCategory().getId();

        // 서브 카테고리 삭제 전 해당 카테고리에 속한 서브카테고리 수를 확인
        long count = subCategoryRepository.countByCategoryId(categoryId);

        // 서브카테고리 삭제
        subCategoryRepository.deleteById(subCategoryId);

        // 삭제 후 서브카테고리 수가 0이면 카테고리를 업데이트
        if (count == 1) {
            Category category = categoryRepository.findById(categoryId).orElseThrow();
            category.updateCategory(new BaseCategoryEnumVo(category.getCategoryName(), false));
            categoryRepository.save(category); // 변경사항을 저장
        }
    }

    public void updateSubCategory(Long subCategoryId, String subCategoryName) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElseThrow();
        subCategory.updateSubCategory(subCategoryName);
    }
}
