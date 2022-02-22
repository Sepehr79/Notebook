package com.kucess.notebook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kucess.notebook.controller.response.ExceptionResponse;
import com.kucess.notebook.model.io.AdminIO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:test3")
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String URL = "/notebook/v1/admins";

    @Test
    void adminControllerCreateReadAndDeleteTest() throws Exception {
        AdminIO adminIO = AdminIO.builder()
                .name("AAA")
                .lastName("BBB")
                .userName("CCC")
                .password("DDD")
                .build();

        // Create new admin user
        perform(post(URL)
                .contentType("application/json")
                .content(OBJECT_MAPPER.writeValueAsString(adminIO)))
                .andExpect(status().isOk())
                .andDo(result -> {
                    Map map = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), Map.class);
                    assertEquals("OK", map.get("status"));
                });

        // Update admin
        perform(put(URL + "/CCC")
                .contentType("application/json")
                .content(OBJECT_MAPPER.writeValueAsString(adminIO.toBuilder().name("KUCESS").userName("sample").build())))
                .andExpect(status().isOk());

        // Get admin with updated attributes
        perform(get(URL + "/sample"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    AdminIO updated = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), AdminIO.class);
                    assertEquals("KUCESS" ,updated.getName());
                });

        // Delete admin
        perform(delete(URL + "/sample"))
                .andExpect(status().isOk());

        // Username not found
        perform(get(URL + "/sample"))
                .andExpect(status().is4xxClientError())
                .andDo(result -> {
                    ExceptionResponse exceptionResponse = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), ExceptionResponse.class);
                    assertEquals(HttpStatus.BAD_REQUEST.name(), exceptionResponse.getResponseType());
                });

    }

    @SneakyThrows
    private ResultActions perform(MockHttpServletRequestBuilder mockHttpServletRequestBuilder){
        return mockMvc.perform(mockHttpServletRequestBuilder);
    }

}
