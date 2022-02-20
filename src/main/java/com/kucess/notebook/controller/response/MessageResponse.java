package com.kucess.notebook.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageResponse extends Response {
    public MessageResponse(String message, String responseType) {
        super(message, responseType);
    }
}
