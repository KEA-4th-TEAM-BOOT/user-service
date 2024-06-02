package userservice.dto.response;

public record SubCategoryResponseDto(
        Long subCategoryId,
        String subCategoryName,
        Long count
) {
}
