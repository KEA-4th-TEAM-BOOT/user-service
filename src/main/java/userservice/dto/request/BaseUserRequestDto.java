package userservice.dto.request;


import jakarta.validation.constraints.NotNull;

public record BaseUserRequestDto(
        @NotNull String name,
        @NotNull String email,
        @NotNull String password,
        @NotNull String nickname,
        @NotNull String profileUrl,
        @NotNull String userLink,
        @NotNull String introduce,
        @NotNull String categoryName
) {
}
