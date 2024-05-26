package ru.gpb.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateUserRequest", description = "Запрос на создание нового пользователя")
public record CreateUserRequest(
        @JsonProperty("userId")
        @Schema(name = "userId", example = "348741706", description = "Идентификатор пользователя в Telegram", requiredMode = Schema.RequiredMode.REQUIRED)
        long userId
) {
}
