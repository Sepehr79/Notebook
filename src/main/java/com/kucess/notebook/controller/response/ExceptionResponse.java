package com.kucess.notebook.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExceptionResponse extends Response {

    public ExceptionResponse(String message, String responseType) {
        super(message, responseType);
    }
}
