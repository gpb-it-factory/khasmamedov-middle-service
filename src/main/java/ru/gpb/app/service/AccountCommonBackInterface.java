package ru.gpb.app.service;

import ru.gpb.app.dto.AccountListResponse;
import ru.gpb.app.dto.CreateAccountRequest;

import java.util.List;

public interface AccountCommonBackInterface {

    AccountCreationStatus createAccount(CreateAccountRequest request);

    AccountRetreivalStatus getAccounts(Long userId);
}
