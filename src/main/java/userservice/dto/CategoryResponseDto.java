package userservice.dto;

import java.util.List;

public record CategoryResponseDto(
        String categoryName,
        Boolean existSubCategory
)
{
    public String of(String categoryName) {
        return categoryName;
    }
}
