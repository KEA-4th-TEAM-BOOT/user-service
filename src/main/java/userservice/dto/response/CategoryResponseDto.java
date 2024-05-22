package userservice.dto.response;


public record CategoryResponseDto(
        String categoryName,
        Boolean existSubCategory
) {

}
