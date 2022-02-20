package com.kucess.notebook.model.service.exception;

import lombok.Getter;

@Getter
public class UserNameNotFoundException extends RuntimeException {

    private static final String MESSAGE = "User not found with the given username";

    private final String userName;

    public UserNameNotFoundException(String userName) {
        super(MESSAGE + ": " + userName);
        this.userName = userName;
    }
}
