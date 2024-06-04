package userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.domain.SubCategory;
import userservice.dto.response.SubCategoryResponseDto;
import userservice.service.SubCategoryService;

import java.util.List;

@RestController
@RequestMapping("/subCategory")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @Operation(summary = "하위 카테고리 생성", description = "지정된 상위 카테고리 내에 새로운 하위 카테고리를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "하위 카테고리 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/{category_id}")
    public ResponseEntity<Void> createSubCategory(@PathVariable Long category_id, @RequestBody String subCategoryName) {
        subCategoryService.createSubCategory(category_id, subCategoryName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "하위 카테고리 목록 조회", description = "지정된 상위 카테고리 내에 존재하는 모든 하위 카테고리 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "하위 카테고리 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "상위 카테고리를 찾을 수 없음")
    })
    @GetMapping("/{category_id}")
    public ResponseEntity<List<SubCategoryResponseDto>> getSubCategory(@PathVariable Long category_id) {
        List<SubCategoryResponseDto> subCategoryList = subCategoryService.getSubCategoryList(category_id);
        return ResponseEntity.ok(subCategoryList);
    }

    @Operation(summary = "하위 카테고리 삭제", description = "지정된 하위 카테고리를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "하위 카테고리 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "하위 카테고리를 찾을 수 없음")
    })
    @DeleteMapping("/{subCategory_id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long subCategory_id) {
        subCategoryService.deleteSubCategory(subCategory_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "하위 카테고리 수정", description = "지정된 하위 카테고리의 이름을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "하위 카테고리 수정 성공"),
            @ApiResponse(responseCode = "404", description = "하위 카테고리를 찾을 수 없음")
    })
    @PatchMapping("/{subCategory_id}")
    public ResponseEntity<Void> updateSubCategory(@PathVariable Long subCategory_id, String subCategoryName) {
        subCategoryService.updateSubCategory(subCategory_id, subCategoryName);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
