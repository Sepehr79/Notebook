package com.kucess.notebook.model.service.exception;

import lombok.Getter;

@Getter
public class DuplicateUsernameException extends Exception{

    private static final String MESSAGE = "Username already taken";

    public DuplicateUsernameException(final String username) {
        super(MESSAGE + ": " + username);
    }
}
