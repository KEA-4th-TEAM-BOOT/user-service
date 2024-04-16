package userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.dto.response.CategoryResponseDto;
import userservice.service.CategoryService;
import userservice.vo.BaseCategoryEnumVo;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService controllerService) {
        this.categoryService = controllerService;
    }

    @PostMapping("/category/{id}")
    public ResponseEntity<String> createCategory(@PathVariable Long id, @RequestBody String categoryName) {
        categoryService.createCategory(id, categoryName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/category/{id}")
    public List<CategoryResponseDto> getCategory(@PathVariable Long id) {
        return categoryService.getCategoryList(id);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/category/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long id, @RequestBody BaseCategoryEnumVo baseCategoryEnumVo) {
        categoryService.updateCategory(id, baseCategoryEnumVo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}