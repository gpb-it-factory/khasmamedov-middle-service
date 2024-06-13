package ru.gpb.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.gpb.app.dto.CreateUserRequest;
import ru.gpb.app.dto.UserResponse;

@Service
@Slf4j
public class UserMiddleService {

    private final RestTemplate restTemplate;

    @Autowired
    public UserMiddleService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean createUser(CreateUserRequest request) {
        boolean result;

        try {
            log.info("Sending request to service C");
            ResponseEntity<Void> response = restTemplate.postForEntity("/users", request, Void.class);
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                log.info("Successfully send request to service C");
                result = true;
            } else {
                log.error("Unexpected code-response while user registration: {}", response.getStatusCode());
                result = false;
            }
        } catch (HttpStatusCodeException e) {
            log.error("HttpStatusCodeException happened in program: ", e);
            result = false;
        } catch (Exception e) {
            log.error("Something serious happened in program: ", e);
            result = false;
        }
        return result;
    }

    public UserCheckStatus isUserRegistered(Long userId) {
        UserCheckStatus userCheckStatus;

        try {
            HttpStatus statusCode =
                    restTemplate.getForEntity("users/" + userId, UserResponse.class).getStatusCode();
            if (statusCode == HttpStatus.OK) {
                userCheckStatus = UserCheckStatus.REGISTERED;
            } else if (statusCode == HttpStatus.NOT_FOUND) {
                userCheckStatus = UserCheckStatus.NOT_REEGISTRED;
            } else {
                userCheckStatus = UserCheckStatus.ERROR;
            }
        } catch (RestClientException e) {
            log.error("RestClientException happened in program: ", e);
            userCheckStatus = UserCheckStatus.ERROR;
        } catch (Exception e) {
            log.error("Something serious happened in program: ", e);
            userCheckStatus = UserCheckStatus.ERROR;
        }
        return userCheckStatus;
    }
}