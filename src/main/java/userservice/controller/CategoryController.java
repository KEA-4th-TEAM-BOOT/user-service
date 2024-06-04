package userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "카테고리 생성", description = "사용자가 새로운 카테고리를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "카테고리 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("")
    public ResponseEntity<Void> createCategory(@RequestHeader("Authorization") String token, @RequestBody String categoryName) {
        categoryService.createCategory(token, categoryName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "카테고리 목록 조회", description = "사용자가 모든 카테고리 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공")
    })
    @GetMapping("")
    public List<CategoryResponseDto> getCategory(HttpServletRequest httpServletRequest) {
        return categoryService.getCategoryList(httpServletRequest);
    }

    @Operation(summary = "카테고리 삭제", description = "사용자가 특정 카테고리를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "카테고리 수정", description = "사용자가 특정 카테고리의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    })
    @PatchMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long categoryId, @RequestBody BaseCategoryEnumVo baseCategoryEnumVo) {
        categoryService.updateCategory(categoryId, baseCategoryEnumVo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "게시물 수 증가", description = "특정 카테고리의 게시물 수를 증가시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시물 수 증가 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    })
    @PostMapping("/{categoryId}")
    public ResponseEntity<Void> IncreasePostCount(@PathVariable Long categoryId){
        categoryService.increasePostCount(categoryId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "사용자별 카테고리 목록 조회", description = "특정 사용자의 카테고리 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자별 카테고리 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<List<CategoryResponseDto>> getUserCategoryList(@PathVariable Long userId){
        List<CategoryResponseDto> categoryList = categoryService.getUserCategoryList(userId);
        return ResponseEntity.ok(categoryList);
    }
}