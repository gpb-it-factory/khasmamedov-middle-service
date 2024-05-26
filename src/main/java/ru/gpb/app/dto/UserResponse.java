package ru.gpb.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

//а нужен ли он тут вообще ? (и если да, где он используется - репа/бд ?) - тогда не войд возвращаемое...
//или используется, но на миддл-слое. где и должна быть обработка ошибок + сохранение в БД, на первом слое - только результат
//т.е. респонс и ошибки - туда. но тогда у нас только реквест, что тоже странно.

@Schema(name = "UserResponse", description = "Информация о пользователе")
public record UserResponse(
        @JsonProperty("userId")
        @Schema(name = "userId", example = "2d7b7a7a-680e-422e-9cc3-23c68e2ff398", description = "Идентификатор пользователя в backend-сервисе", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        UUID userId
) {}