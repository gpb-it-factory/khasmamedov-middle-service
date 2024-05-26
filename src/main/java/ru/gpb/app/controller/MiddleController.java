package ru.gpb.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gpb.app.dto.CreateUserRequest;
import ru.gpb.app.dto.UserResponse;
import ru.gpb.app.service.UserMiddleService;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MiddleController {

    private final UserMiddleService userMiddleService;

    @Autowired
    public MiddleController(UserMiddleService userMiddleService) {
        this.userMiddleService = userMiddleService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        Optional<UserResponse> response = userMiddleService.createUser(request);
        return response.isPresent() ? ResponseEntity.ok(response.get()) : ResponseEntity.noContent().build();
    }
}