package userservice.dto.request;


import jakarta.validation.constraints.NotNull;
import org.springframework.data.relational.core.sql.In;

import java.time.LocalDateTime;

public record BaseUserRequestDto(
        @NotNull String name,
        @NotNull String email,
        @NotNull String password,
        @NotNull String nickname,
        @NotNull String blogUrl
) {
}
