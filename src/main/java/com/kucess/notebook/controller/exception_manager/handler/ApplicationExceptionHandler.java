package com.kucess.notebook.controller.exception_manager.handler;

import com.kucess.notebook.controller.response.ExceptionResponse;
import com.kucess.notebook.model.service.exception.UserNameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(UserNameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNameNotFound(){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                new ExceptionResponse("Incorrect username or password", httpStatus.name()), httpStatus
        );
    }

}
