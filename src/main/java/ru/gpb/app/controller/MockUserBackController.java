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
@RequestMapping("/mock")
@Slf4j
public class MockUserBackController {

    private final Random random = new Random();

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        log.info("Getting query from middle-service... ");
        log.info("Got userId: {}", request.userId());
        if (random.nextBoolean()) {
            log.info("Cannot generate UUID...");
            log.info("Returning error...");
            Error error = new Error(
                    "Произошло что-то ужасное, но станет лучше, честно",
                    "GeneralError",
                    "123",
                    UUID.randomUUID()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } else {
            log.info("User is successfully created");
            return ResponseEntity.noContent().build();
        }
    }
}
