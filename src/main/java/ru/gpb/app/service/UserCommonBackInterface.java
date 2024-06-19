package ru.gpb.app.service;

import ru.gpb.app.dto.CreateUserRequest;

public interface UserCommonBackInterface {

    public UserCreationStatus createUser(CreateUserRequest request);
}
