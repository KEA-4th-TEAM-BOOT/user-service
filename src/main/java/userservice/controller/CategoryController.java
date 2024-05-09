package userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.dto.response.CategoryResponseDto;
import userservice.service.CategoryService;
import userservice.vo.BaseCategoryEnumVo;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService controllerService) {
        this.categoryService = controllerService;
    }

    @PostMapping("/{user_id}")
    public ResponseEntity<String> createCategory(@PathVariable Long user_id, @RequestBody String categoryName) {
        categoryService.createCategory(user_id, categoryName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{user_id}")
    public List<CategoryResponseDto> getCategory(@PathVariable Long user_id) {
        return categoryService.getCategoryList(user_id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long id, @RequestBody BaseCategoryEnumVo baseCategoryEnumVo) {
        categoryService.updateCategory(id, baseCategoryEnumVo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}