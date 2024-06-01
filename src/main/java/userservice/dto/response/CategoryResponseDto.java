package userservice.dto.response;


import userservice.domain.SubCategory;

import java.util.List;

public record CategoryResponseDto(
        Long categoryId,
        String categoryName,
        Boolean existSubCategory,
        Long count,
        List<SubCategoryResponseDto> subCategoryList
) {

}
