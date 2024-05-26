package ru.gpb.app.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.gpb.app.dto.CreateUserRequest;
import ru.gpb.app.dto.UserResponse;

import java.util.Optional;

@Service
@Slf4j
public class UserMiddleService {

    private final RestTemplate restTemplate;
    private final String backUrl;

    @Autowired
    public UserMiddleService(RestTemplate restTemplate, @Value("${service-c.url}") String backUrl) {
        this.restTemplate = restTemplate;
        this.backUrl = backUrl;
    }

    public Optional<UserResponse> createUser(CreateUserRequest request) {
        String url = backUrl + "/users";
        try {
            log.info("Sending query to C service: {}", url);
            ResponseEntity<UserResponse> response = restTemplate.postForEntity(url, request, UserResponse.class);
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                log.info("Something is wrong with registration, 204 code is returned");
                return Optional.empty();
            } else if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("The returned ID is: {}", response.getBody().userId());
                return Optional.of(response.getBody());
            } else {
                log.error("Something is wrong wuth registration, getting code: {}", response.getStatusCode());
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Something is VERY wrong, the problem: ", e);
            return Optional.empty();
        }
    }
}