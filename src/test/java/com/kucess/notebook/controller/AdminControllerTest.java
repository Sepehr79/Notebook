package com.kucess.notebook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.response.ExceptionResponse;
import com.kucess.notebook.model.response.Message;
import com.kucess.notebook.model.response.StatusResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:test3")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class AdminControllerTest {

    private static final String JSON = "application/json";

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String URL = "/notebook/v1/admins";

    private final static AdminIO ADMIN_IO = AdminIO.builder()
            .name("AAA")
            .lastName("BBB")
            .userName("CCC")
            .password("DDD")
            .build();

    @SneakyThrows
    @Test
    @Order(1)
    void registerAdmin(){
        perform(
                post(URL).contentType(JSON).content(OBJECT_MAPPER.writeValueAsString(ADMIN_IO)),
                status().isOk(),
                result -> {
                    StatusResponse response = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), StatusResponse.class);
                    assertEquals(Message.ADMIN_REGISTERED, response.getMessage());
                }
        );
    }

    @Test
    @Order(2)
    @SneakyThrows
    void updateAdmin(){
        perform(
                put(URL + "/CCC").contentType("application/json")
                        .content(OBJECT_MAPPER.writeValueAsString(ADMIN_IO.toBuilder().name("KUCESS").userName("sample").build())),
                status().isOk(),
                print()
        );
    }

    @Test
    @Order(3)
    @SneakyThrows
    void getAdmin(){
        perform(
                get(URL + "/sample"),
                status().isOk(),
                result -> {
                    AdminIO updated = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), AdminIO.class);
                    assertEquals("KUCESS" ,updated.getName());
                }
        );
    }

    @Test
    @Order(4)
    @SneakyThrows
    void deleteAdmin(){
        perform(
                delete(URL + "/sample"),
                status().isOk(),
                result -> {
                    Map<String, String> properties = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), StatusResponse.class)
                            .getProperties();
                    assertEquals("sample" ,properties.get("username"));
                }
        );
    }

    @Test
    @Order(5)
    @SneakyThrows
    void onUsernameNotFound(){
        perform(
                get(URL + "/sample"),
                status().isBadRequest(),
                result -> {
                    ExceptionResponse exceptionResponse = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), ExceptionResponse.class);
                    assertEquals(HttpStatus.BAD_REQUEST.name(), exceptionResponse.getResponseType());
                }
        );
    }

    @SneakyThrows
    private void perform(MockHttpServletRequestBuilder requestBuilder,
                         ResultMatcher resultMatcher,
                         ResultHandler resultHandler){
        mockMvc.perform(requestBuilder)
                .andExpect(resultMatcher)
                .andDo(resultHandler);

    }

}
