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

    private final RestBackClient restBackClient;

    public UserMiddleService(RestBackClient restBackClient) {
        this.restBackClient = restBackClient;
    }

    public UserCreationStatus createUser(CreateUserRequest request) {
        return restBackClient.createUser(request);
    }

    public AccountCreationStatus createAccount(CreateAccountRequest request) {
        return restBackClient.createAccount(request);
    }

    public boolean getUserById(Long userId) {
        return restBackClient.getUserById(userId);
    }

    public AccountRetreivalStatus getAccountsById(Long userId) {
        return restBackClient.getAccounts(userId);
    }
}