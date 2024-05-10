package userservice.dto.request;

import jakarta.validation.constraints.Email;

public record EmailAuthRequestDto(
        @Email
        String email,

        String authNumber
) {
}
