package com.kucess.notebook.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExceptionResponse extends Response {

    private String responseType;

    public ExceptionResponse(String message, String responseType) {
        super(message);
        this.responseType = responseType;
    }
}
