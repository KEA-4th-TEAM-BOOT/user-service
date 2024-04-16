package userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.domain.SubCategory;
import userservice.service.SubCategoryService;

import java.util.List;

@RestController
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @PostMapping("/subCategory/{category_id}")
    public ResponseEntity<Void> createSubCategory(@PathVariable Long category_id, @RequestBody String subCategoryName){
        subCategoryService.createSubCategory(category_id, subCategoryName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/subCategory/{category_id}")
    public ResponseEntity<List<String>> getSubCategory(@PathVariable Long category_id){
        List<String> subCategoryList = subCategoryService.getSubCategoryList(category_id);
        return ResponseEntity.ok(subCategoryList);
    }

    @DeleteMapping("/subCategory/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long id){
        subCategoryService.deleteSubCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/subCategory/{id}")
    public ResponseEntity<Void> updateSubCategory(@PathVariable Long id, String subCategoryName){
        subCategoryService.updateSubCategory(id, subCategoryName);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
