package ru.gpb.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.gpb.app.dto.CreateUserRequest;
import ru.gpb.app.dto.UserResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Service
@Slf4j
public class UserMiddleService {

    private final RestTemplate restTemplate;

    @Autowired
    public UserMiddleService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean createUser(CreateUserRequest request) {
        try {
            log.info("Sending request to service C");
            ResponseEntity<Void> response = restTemplate.postForEntity("/users", request, Void.class);
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                log.info("Successfully send request to service C");
                return true;
            } else {
                log.error("Unexpected code-response while user registration: {}", response.getStatusCode());
                return false;
            }
        } catch (HttpStatusCodeException e) {
            log.error("HttpStatusCodeException exception in program: ", e);
            return false;
        } catch (Exception e) {
            log.error("Something serious happened in program: ", e);
            return false;
        }
    }
}