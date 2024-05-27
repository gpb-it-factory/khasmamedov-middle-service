package ru.gpb.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gpb.app.dto.CreateUserRequest;
import ru.gpb.app.dto.Error;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/mock/users")
@Slf4j
public class MockUserBackController {

    private final Random random = new Random();

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        log.info("Получил запрос от миддл-сервиса... ");
        log.info("Полученный юзерАйди: {}", request.userId());
        if (random.nextBoolean()) {
            log.info("Не могу сгенерировать UUID...");
            log.info("Возвращаю ошибку...");
            Error error = new Error(
                    "Произошло что-то ужасное, но станет лучше, честно",
                    "GeneralError",
                    "123",
                    UUID.randomUUID()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } else {
            log.info("Пользователь создан успешно");
            return ResponseEntity.noContent().build();
        }
    }
}
