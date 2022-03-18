package com.kucess.notebook.controller.exception_manager.handler;

import com.kucess.notebook.model.response.ExceptionResponse;
import com.kucess.notebook.model.service.exception.DuplicateUsernameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    public static final String INDEX_NOT_FOUND = "Activity index not found";

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNameNotFound(){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                new ExceptionResponse("Incorrect username or password", httpStatus.name()), httpStatus
        );
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<ExceptionResponse> handleIndexOutOfBound(){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                new ExceptionResponse(INDEX_NOT_FOUND, httpStatus.name()), httpStatus
        );
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateUsername(DuplicateUsernameException duplicateUsernameException){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                new ExceptionResponse(duplicateUsernameException.getMessage() ,httpStatus.name()), httpStatus
        );
    }

}
