package ru.gpb.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.gpb.app.dto.AccountListResponse;
import ru.gpb.app.dto.CreateAccountRequest;
import ru.gpb.app.dto.CreateUserRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RestBackClient implements UserCommonBackInterface, AccountCommonBackInterface {

    private final RestTemplate restTemplate;

    public RestBackClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private UserCreationStatus getUserCreationStatus(ResponseEntity<Void> response) {
        UserCreationStatus userCreationStatus;

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

        return userCreationStatus;
    }

    @Override
    public UserCreationStatus createUser(CreateUserRequest request) {
        UserCreationStatus userCreationStatus;

        try {
            log.info("Sending request to service C to create user");
            ResponseEntity<Void> response = restTemplate.postForEntity("/users", request, Void.class);
            userCreationStatus = getUserCreationStatus(response);
        } catch (HttpStatusCodeException e) {
            log.error("HttpStatusCodeException happened in program while user creation: ", e);
            userCreationStatus = UserCreationStatus.USER_ERROR;
        } catch (Exception e) {
            log.error("Something serious happened in program while user creation: ", e);
            userCreationStatus = UserCreationStatus.USER_ERROR;
        }

        return userCreationStatus;
    }

    private AccountCreationStatus getAccountCreationStatus(ResponseEntity<Void> response) {
        AccountCreationStatus accountCreationStatus;

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

        return accountCreationStatus;
    }
    @Override
    public AccountCreationStatus createAccount(CreateAccountRequest request) {
        AccountCreationStatus accountCreationStatus;

        try {
            log.info("Sending request to service C to create account");
            String url = String.format("/users/%d/accounts", request.userId());
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);
            accountCreationStatus = getAccountCreationStatus(response);
        } catch (HttpStatusCodeException e) {
            log.error("HttpStatusCodeException happened in program while account creation: ", e);
            accountCreationStatus = AccountCreationStatus.ACCOUNT_ERROR;
        } catch (Exception e) {
            log.error("Something serious happened in program while account creation: ", e);
            accountCreationStatus = AccountCreationStatus.ACCOUNT_ERROR;
        }

        return accountCreationStatus;
    }

    @Override
    public boolean getUserById(Long userId) {
        log.info("Sending request to service C to check user");
        String url = String.format("/users/%d", userId);
        ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
        try {
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Successfully send request to service C, user with {} is found", userId);
                return true;
            } else {
                log.info("Successfully send request to service C, user with {} is not found", userId);
                return false;
            }
        } catch (HttpStatusCodeException e) {
            log.error("HttpStatusCodeException happened in program while checking if user is registered: ", e);
            return false;
        } catch (Exception e) {
            log.error("Something serious happened in program while checking if user is registered: ", e);
            return false;
        }
    }

    @Override
    public AccountRetreivalStatus getAccounts(Long userId) {
        log.info("Sending request to service C to get accounts for user {}", userId);

        AccountRetreivalStatus accountRetreivalStatus = null;
        try {
            String url = String.format("/users/%d/accounts", userId);
            ResponseEntity<AccountListResponse[]> accountsEntity = restTemplate.getForEntity(url, AccountListResponse[].class);
            if (accountsEntity.getStatusCode() == HttpStatus.OK) {
                AccountListResponse[] accounts = accountsEntity.getBody();
                if (accounts != null && accounts.length > 0) {
                    log.info("Successfully send request to service C, accounts for user with {} are found", userId);
                    accountRetreivalStatus = AccountRetreivalStatus.ACCOUNTS_FOUND;
                    accountRetreivalStatus.setAccountListResponses(Arrays.asList(accounts));
                }
                log.info("Successfully send request to service C, NO accounts for user with {} are found", userId);
                accountRetreivalStatus = AccountRetreivalStatus.ACCOUNTS_NOT_FOUND;
                accountRetreivalStatus.setAccountListResponses(Collections.emptyList());
            }
        } catch (HttpStatusCodeException e) {
            log.error("HttpStatusCodeException happened in program while retreiving accounts: ", e);
            accountRetreivalStatus = AccountRetreivalStatus.ACCOUNTS_ERROR;
        } catch (Exception e) {
            log.error("Something serious happened in program while retreiving accounts: ", e);
            accountRetreivalStatus = AccountRetreivalStatus.ACCOUNTS_ERROR;
        }
        return accountRetreivalStatus;
    }
}
