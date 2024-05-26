package ru.gpb.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

//Вот здесь вроде бы больше к месту ?
@Schema(name = "Error", description = "Формат непредвиденной ошибки")
public record Error(
        @JsonProperty("message")
        @Schema(name = "message", example = "Произошло что-то ужасное, но станет лучше, честно")
        String message,

        @JsonProperty("type")
        @Schema(name = "type", example = "GeneralError")
        String type,

        @JsonProperty("code")
        @Schema(name = "code", example = "123")
        String code,

        @JsonProperty("trace_id")
        @Schema(name = "trace_id", example = "5f59e024-03c7-498d-9fc9-b8b15fd49c47")
        UUID traceId
) {}