package ru.gpb.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gpb.app.dto.CreateUserRequest;
import ru.gpb.app.dto.UserResponse;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/mock/users")
@Slf4j
public class MockUserBackController {

    private final Random random = new Random();

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        log.info("Getting request from service B, processing... ");
        log.info("userID is: " + request.userId());
        if (random.nextBoolean()) {
            log.info("Cannot generate UUID...");
            log.info("Returning 204...");
            return ResponseEntity.noContent().build();
        } else {
            UserResponse response = new UserResponse(UUID.randomUUID());
            log.info("Generating UUID... " + response.userId());
            log.info("Returning 200...");
            return ResponseEntity.ok(response);
        }
    }
}
