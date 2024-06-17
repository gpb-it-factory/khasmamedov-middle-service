package ru.gpb.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.gpb.app.dto.CreateAccountRequest;
import ru.gpb.app.dto.CreateUserRequest;

import java.util.UUID;

@Service
@Slf4j
public class UserMiddleService {

    private final RestTemplate restTemplate;

    @Autowired
    public UserMiddleService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserCreationStatus createUser(CreateUserRequest request) {
        UserCreationStatus userCreationStatus;

        try {
            log.info("Sending request to service C to create user");
            ResponseEntity<Void> response = restTemplate.postForEntity("/users", request, Void.class);
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                log.info("Successfully send request to service C for user creation");
                userCreationStatus = UserCreationStatus.USER_CREATED;
            } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                log.warn("User already exists");
                userCreationStatus = UserCreationStatus.USER_ALREADY_EXISTS;
            } else {
                log.error("Unexpected code-response while user registration: {}", response.getStatusCode());
                userCreationStatus = UserCreationStatus.USER_ERROR;
            }
        } catch (HttpStatusCodeException e) {
            log.error("HttpStatusCodeException happened in program while user creation: ", e);
            userCreationStatus = UserCreationStatus.USER_ERROR;
        } catch (Exception e) {
            log.error("Something serious happened in program while user creation: ", e);
            userCreationStatus = UserCreationStatus.USER_ERROR;
        }

        return userCreationStatus;
    }

    public AccountCreationStatus createAccount(CreateAccountRequest request) {
        AccountCreationStatus accountCreationStatus;

        try {
            log.info("Sending request to service C to create account");
            String url = String.format("/users/%d/accounts", request.userId());
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                log.info("Successfully send request to service C for account creation");
                accountCreationStatus = AccountCreationStatus.ACCOUNT_CREATED;
            } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                log.warn("Account already exists");
                accountCreationStatus = AccountCreationStatus.ACCOUNT_ALREADY_EXISTS;
            } else {
                log.error("Unexpected code-response while account creation: {}", response.getStatusCode());
                accountCreationStatus = AccountCreationStatus.ACCOUNT_ERROR;
            }
        } catch (HttpStatusCodeException e) {
            log.error("HttpStatusCodeException happened in program while account creation: ", e);
            accountCreationStatus = AccountCreationStatus.ACCOUNT_ERROR;
        } catch (Exception e) {
            log.error("Something serious happened in program while account creation: ", e);
            accountCreationStatus = AccountCreationStatus.ACCOUNT_ERROR;
        }

        return accountCreationStatus;
    }
}