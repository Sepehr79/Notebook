package com.kucess.notebook.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class StatusResponse extends Response{

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> properties;

    public StatusResponse(String message, Map<String, String> properties) {
        super(message);
        this.properties = properties;
    }

    public StatusResponse(String message) {
        super(message);
    }
}
