package userservice.dto.request;

public record LoginRequestDto(
        String email,
        String password
) {
}
