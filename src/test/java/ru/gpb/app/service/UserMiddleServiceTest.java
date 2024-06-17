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
    private RestTemplate restTemplate;

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
    public void createUserReturned204() {
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        when(restTemplate.postForEntity(userCreateUrl, properRequestId, Void.class))
                .thenReturn(response);

        UserCreationStatus result = middleService.createUser(properRequestId);

        assertThat(result).isEqualTo(UserCreationStatus.USER_CREATED);
        verify(restTemplate, times(1))
                .postForEntity(userCreateUrl, properRequestId, Void.class);
    }

    @Test
    public void createAccountReturned204() {
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        when(restTemplate.postForEntity(accountCreateUrl, properAccountRequest, Void.class))
                .thenReturn(response);

        AccountCreationStatus result = middleService.createAccount(properAccountRequest);

        assertThat(result).isEqualTo(AccountCreationStatus.ACCOUNT_CREATED);
        verify(restTemplate, times(1))
                .postForEntity(accountCreateUrl, properAccountRequest, Void.class);
    }

    @Test
    public void createUserReturned409() {
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CONFLICT);
        when(restTemplate.postForEntity(userCreateUrl, properRequestId, Void.class))
                .thenReturn(response);

        UserCreationStatus result = middleService.createUser(properRequestId);

        assertThat(result).isEqualTo(UserCreationStatus.USER_ALREADY_EXISTS);
        verify(restTemplate, times(1))
                .postForEntity(userCreateUrl, properRequestId, Void.class);
    }

    @Test
    public void createAccountReturned409() {
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CONFLICT);
        when(restTemplate.postForEntity(accountCreateUrl, properAccountRequest, Void.class))
                .thenReturn(response);

        AccountCreationStatus result = middleService.createAccount(properAccountRequest);

        assertThat(result).isEqualTo(AccountCreationStatus.ACCOUNT_ALREADY_EXISTS);
        verify(restTemplate, times(1))
                .postForEntity(accountCreateUrl, properAccountRequest, Void.class);
    }

    @Test
    public void userCreateReturnedNot204Or409() {
        @SuppressWarnings("unchecked")
        ResponseEntity<Void> mockedResponse = mock(ResponseEntity.class);
        when(mockedResponse.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(restTemplate.postForEntity(userCreateUrl, improperRequestId, Void.class))
                .thenReturn(mockedResponse);

        UserCreationStatus result = middleService.createUser(improperRequestId);

        assertThat(result).isEqualTo(UserCreationStatus.USER_ERROR);
        verify(restTemplate, times(1))
                .postForEntity(userCreateUrl, improperRequestId, Void.class);
    }

    @Test
    public void accountCreateReturnedNot204Or409() {
        @SuppressWarnings("unchecked")
        ResponseEntity<Void> mockedResponse = mock(ResponseEntity.class);
        when(mockedResponse.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(restTemplate.postForEntity(accountCreateUrl, properAccountRequest, Void.class))
                .thenReturn(mockedResponse);

        AccountCreationStatus result = middleService.createAccount(properAccountRequest);

        assertThat(result).isEqualTo(AccountCreationStatus.ACCOUNT_ERROR);
        verify(restTemplate, times(1))
                .postForEntity(accountCreateUrl, properAccountRequest, Void.class);
    }

    @Test
    public void createUserResultedInHttpStatusCodeException() {
        HttpStatusCodeException httpStatusCodeException = new HttpStatusCodeException(HttpStatus.INTERNAL_SERVER_ERROR) {
        };
        when(restTemplate.postForEntity(userCreateUrl, wrongRequestId, Void.class))
                .thenThrow(httpStatusCodeException);

        UserCreationStatus result = middleService.createUser(wrongRequestId);

        assertThat(result).isEqualTo(UserCreationStatus.USER_ERROR);
        verify(restTemplate, times(1))
                .postForEntity(userCreateUrl, wrongRequestId, Void.class);
    }

    @Test
    public void createAccountResultedInHttpStatusCodeException() {
        HttpStatusCodeException httpStatusCodeException = new HttpStatusCodeException(HttpStatus.INTERNAL_SERVER_ERROR) {
        };
        when(restTemplate.postForEntity(accountCreateUrl, properAccountRequest, Void.class))
                .thenThrow(httpStatusCodeException);

        AccountCreationStatus result = middleService.createAccount(properAccountRequest);

        assertThat(result).isEqualTo(AccountCreationStatus.ACCOUNT_ERROR);
        verify(restTemplate, times(1))
                .postForEntity(accountCreateUrl, properAccountRequest, Void.class);
    }

    @Test
    public void createUserQuerySuccessfullySendAndReturnedGeneralException() {
        RuntimeException seriousException = new RuntimeException("Serious exception") {
        };
        when(restTemplate.postForEntity(userCreateUrl, wrongRequestId, Void.class))
                .thenThrow(seriousException);

        UserCreationStatus result = middleService.createUser(wrongRequestId);

        assertThat(result).isEqualTo(UserCreationStatus.USER_ERROR);
        verify(restTemplate, times(1))
                .postForEntity(userCreateUrl, wrongRequestId, Void.class);
    }

    @Test
    public void createAccountQuerySuccessfullySendAndReturnedGeneralException() {
        RuntimeException seriousException = new RuntimeException("Serious exception") {
        };
        when(restTemplate.postForEntity(accountCreateUrl, properAccountRequest, Void.class))
                .thenThrow(seriousException);

        AccountCreationStatus result = middleService.createAccount(properAccountRequest);

        assertThat(result).isEqualTo(AccountCreationStatus.ACCOUNT_ERROR);
        verify(restTemplate, times(1))
                .postForEntity(accountCreateUrl, properAccountRequest, Void.class);
    }
}