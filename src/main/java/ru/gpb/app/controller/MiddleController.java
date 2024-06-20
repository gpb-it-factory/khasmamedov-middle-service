package ru.gpb.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gpb.app.dto.CreateAccountRequest;
import ru.gpb.app.dto.CreateUserRequest;
import ru.gpb.app.dto.Error;
import ru.gpb.app.service.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@Validated
@RestController
@RequestMapping("/v2/api")
public class MiddleController {

    private final UserMiddleService userMiddleService;
    private final GlobalExceptionHandler globalExceptionHandler;


    @Autowired
    public MiddleController(UserMiddleService userMiddleService, GlobalExceptionHandler globalExceptionHandler) {
        this.userMiddleService = userMiddleService;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    private ResponseEntity<?> handlerForUserCreation(UserCreationStatus userCreationStatus) {
        ResponseEntity<?> responseEntity = null;
        switch (userCreationStatus) {
            case USER_CREATED -> responseEntity = ResponseEntity.noContent().build();
            case USER_ALREADY_EXISTS -> responseEntity = globalExceptionHandler.errorResponseEntityBuilder(
                    "Пользователь уже зарегистрирован",
                    "CurrentUserIsAlreadyRegistered",
                    "409",
                    HttpStatus.CONFLICT
            );
            case USER_ERROR -> responseEntity = globalExceptionHandler.errorResponseEntityBuilder(
                    "Ошибка при регистрации пользователя",
                    "UserCreationError",
                    "500",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        return responseEntity;
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequest request) {
        ResponseEntity<?> responseEntity;

        try {
            UserCreationStatus userCreationStatus = userMiddleService.createUser(request);
            responseEntity = handlerForUserCreation(userCreationStatus);
        } catch (Exception e) {
            responseEntity = globalExceptionHandler.handleGeneralException(e);
        }

        return responseEntity;
    }

    private ResponseEntity<?> handlerForAccountCreation(AccountCreationStatus accountCreationStatus) {
        ResponseEntity<?> responseEntity = null;

        switch (accountCreationStatus) {
            case ACCOUNT_CREATED -> responseEntity = ResponseEntity.noContent().build();

            case ACCOUNT_ALREADY_EXISTS -> responseEntity = globalExceptionHandler.errorResponseEntityBuilder(
                    "Такой счет у данного пользователя уже есть",
                    "AccountAlreadyExists",
                    "409",
                    HttpStatus.CONFLICT
            );
            case ACCOUNT_ERROR -> responseEntity = globalExceptionHandler.errorResponseEntityBuilder(
                    "Ошибка при создании счета",
                    "AccountCreationError",
                    "500",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return responseEntity;
    }

    @PostMapping("/accounts")
    public ResponseEntity<?> createAccount(@RequestBody @Valid CreateAccountRequest request) {
        ResponseEntity<?> responseEntity;

        try {
            UserCreationStatus userCreationStatus =
                    userMiddleService.createUser(new CreateUserRequest(request.userId(), request.userName()));
            if (userCreationStatus != UserCreationStatus.USER_CREATED &&
                    userCreationStatus != UserCreationStatus.USER_ALREADY_EXISTS) {
                return globalExceptionHandler.errorResponseEntityBuilder(
                        "Ошибка при регистрации пользователя",
                        "UserCreationError",
                        "500",
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }

            AccountCreationStatus accountCreationStatus = userMiddleService.createAccount(request);
            responseEntity = handlerForAccountCreation(accountCreationStatus);
        } catch (Exception e) {
            responseEntity = globalExceptionHandler.handleGeneralException(e);
        }
        return responseEntity;
    }

    private Optional<ResponseEntity<Error>> checkUserRetrievalStatus(UserRetrievalStatus userRetrievalStatus) {
        if (userRetrievalStatus == UserRetrievalStatus.USER_NOT_FOUND) {
            return Optional.of(globalExceptionHandler.errorResponseEntityBuilder(
                    "Пользователь не найден",
                    "UserCannotBeFound",
                    "404",
                    HttpStatus.NOT_FOUND
            ));
        } else if (userRetrievalStatus == UserRetrievalStatus.USER_ERROR) {
            return Optional.of(globalExceptionHandler.errorResponseEntityBuilder(
                    "Ошибка при получении пользователя",
                    "UserRetrievingError",
                    "500",
                    HttpStatus.INTERNAL_SERVER_ERROR
            ));
        }
        return Optional.empty();
    }

    private ResponseEntity<?> handlerForRetrievingAccounts(AccountRetrievalStatus accountRetrievalStatus) {
        ResponseEntity<?> responseEntity = null;

        switch (accountRetrievalStatus) {
            case ACCOUNTS_FOUND -> responseEntity = ResponseEntity.ok(accountRetrievalStatus.getAccountListResponses());
            case ACCOUNTS_NOT_FOUND -> responseEntity = ResponseEntity.noContent().build();
            case ACCOUNTS_ERROR -> responseEntity = globalExceptionHandler.errorResponseEntityBuilder(
                    "Ошибка при получении счетов",
                    "AccountRetrievingError",
                    "500",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return responseEntity;
    }

    @GetMapping("users/{userId}/accounts")
    public ResponseEntity<?> getAccount(@PathVariable Long userId) {
        ResponseEntity<?> responseEntity;
        try {
            UserRetrievalStatus userRetrievalStatus = userMiddleService.getUserById(userId);

            Optional<ResponseEntity<Error>> retreivalStatus = checkUserRetrievalStatus(userRetrievalStatus);
            if (retreivalStatus.isPresent()) {
                return retreivalStatus.get();
            }

            AccountRetrievalStatus accounts = userMiddleService.getAccountsById(userId);
            responseEntity = handlerForRetrievingAccounts(accounts);
        } catch (Exception e) {
            responseEntity = globalExceptionHandler.handleGeneralException(e);
        }

        return responseEntity;
    }
}