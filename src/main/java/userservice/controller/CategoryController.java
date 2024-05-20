package userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("")
    public ResponseEntity<Void> createCategory(@RequestHeader("Authorization") String token, @RequestBody String categoryName) {
        categoryService.createCategory(token, categoryName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("")
    public List<CategoryResponseDto> getCategory(HttpServletRequest httpServletRequest) {
        return categoryService.getCategoryList(httpServletRequest);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long categoryId, @RequestBody BaseCategoryEnumVo baseCategoryEnumVo) {
        categoryService.updateCategory(categoryId, baseCategoryEnumVo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}