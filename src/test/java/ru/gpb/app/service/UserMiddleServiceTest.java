package ru.gpb.app.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.gpb.app.dto.CreateAccountRequest;
import ru.gpb.app.dto.CreateUserRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserMiddleServiceTest {

    @Mock
    private RestBackClient restBackClient;

    @InjectMocks
    private UserMiddleService middleService;

    private static CreateUserRequest properRequestId;
    private static CreateUserRequest improperRequestId;
    private static CreateUserRequest wrongRequestId;
    private static CreateAccountRequest properAccountRequest;
    private static String accountCreateUrl;
    private static String userCreateUrl;

    @BeforeAll
    static void setUp() {
        properRequestId = new CreateUserRequest(868047670L, "Khasmamedov");
        improperRequestId = new CreateUserRequest(1234567890L, "Khasmamedov");
        wrongRequestId = new CreateUserRequest(-1234567890L, "Khasmamedov");
        properAccountRequest = new CreateAccountRequest(
                868047670L,
                "Khasmamedov",
                "My first awesome account"
        );
        accountCreateUrl = String.format("/users/%d/accounts", 868047670L);
        userCreateUrl = "/users";
    }

    @Test
    public void createUserWasOK() {
        when(restBackClient.createUser(properRequestId)).thenReturn(UserCreationStatus.USER_CREATED);

        UserCreationStatus result = restBackClient.createUser(properRequestId);

        assertThat(UserCreationStatus.USER_CREATED).isEqualTo(result);
        verify(restBackClient, times(1))
                .createUser(properRequestId);
    }

    @Test
    public void createAccountWasOK() {
        when(restBackClient.createAccount(properAccountRequest)).thenReturn(AccountCreationStatus.ACCOUNT_CREATED);

        AccountCreationStatus result = restBackClient.createAccount(properAccountRequest);

        assertThat(AccountCreationStatus.ACCOUNT_CREATED).isEqualTo(result);
        verify(restBackClient, times(1))
                .createAccount(properAccountRequest);
    }

    @Test
    public void createUserReturnedAlreadyExistedUser() {
        when(restBackClient.createUser(properRequestId)).thenReturn(UserCreationStatus.USER_ALREADY_EXISTS);

        UserCreationStatus result = restBackClient.createUser(properRequestId);

        assertThat(UserCreationStatus.USER_ALREADY_EXISTS).isEqualTo(result);
        verify(restBackClient, times(1))
                .createUser(properRequestId);
    }

    @Test
    public void createAccountReturnedAlreadyExistedAccount() {
        when(restBackClient.createAccount(properAccountRequest)).thenReturn(AccountCreationStatus.ACCOUNT_ALREADY_EXISTS);

        AccountCreationStatus result = restBackClient.createAccount(properAccountRequest);

        assertThat(AccountCreationStatus.ACCOUNT_ALREADY_EXISTS).isEqualTo(result);
        verify(restBackClient, times(1))
                .createAccount(properAccountRequest);
    }

    /**
     * Two following test cover the common variant of returned Error. Actual returned error is tested in RestBackClientTest
     */
    @Test
    public void userCreateReturnedError() {
        when(restBackClient.createUser(improperRequestId)).thenReturn(UserCreationStatus.USER_ERROR);

        UserCreationStatus result = restBackClient.createUser(improperRequestId);

        assertThat(UserCreationStatus.USER_ERROR).isEqualTo(result);
        verify(restBackClient, times(1))
                .createUser(improperRequestId);
    }

    @Test
    public void accountCreateReturnedError() {
        when(restBackClient.createAccount(properAccountRequest)).thenReturn(AccountCreationStatus.ACCOUNT_ERROR);

        AccountCreationStatus result = restBackClient.createAccount(properAccountRequest);

        assertThat(AccountCreationStatus.ACCOUNT_ERROR).isEqualTo(result);
        verify(restBackClient, times(1))
                .createAccount(properAccountRequest);
    }
}