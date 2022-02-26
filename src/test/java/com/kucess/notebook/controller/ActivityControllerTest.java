package com.kucess.notebook.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kucess.notebook.controller.exception_manager.handler.ApplicationExceptionHandler;
import com.kucess.notebook.model.io.ActivityIO;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.io.EmployeeIO;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:employeeController")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActivityControllerTest {

    private static final String PATH = "/notebook/v1/admins/{adm}/employees/{emp}/activities";
    private static final String JSON = "application/json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    private static final AdminIO ADMIN_IO = AdminIO.builder()
            .name("name1")
            .lastName("lastname1")
            .userName("username1")
            .password("1234")
            .build();

    private static final EmployeeIO EMPLOYEE_IO = EmployeeIO.builder()
            .name("name2")
            .lastName("lastname2")
            .userName("username2")
            .password("1234")
            .build();

    private static final ActivityIO ACTIVITY_IO = ActivityIO.builder()
            .activityName("Test")
            .activityDescription("This is a test")
            .score(10)
            .build();


    @SneakyThrows
    @Test
    @Order(1)
    void insertAdmin(){
        perform(
                post("/notebook/v1/admins").contentType(JSON).content(OBJECT_MAPPER.writeValueAsString(ADMIN_IO)),
                status().isOk(),
                print()
        );
    }

    @SneakyThrows
    @Test
    @Order(2)
    void insertEmployee(){
        perform(post("/notebook/v1/admins/username1/employees")
                        .contentType(JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(EMPLOYEE_IO)),
                status().isOk(),
                print());
    }

    @SneakyThrows
    @Test
    @Order(3)
    void addActivity(){
        perform(
                post(PATH.replace("{adm}", "username1").replace("{emp}", "username2"))
                        .contentType(JSON).content(OBJECT_MAPPER.writeValueAsString(ACTIVITY_IO)),
                status().isOk(),
                result -> {
                    StatusResponse statusResponse = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), StatusResponse.class);
                    assertEquals(Message.ACTIVITY_ADDED_TO_EMPLOYEE, statusResponse.getMessage());
                    Map<String, String> properties = statusResponse.getProperties();
                    assertEquals("username1", properties.get("admin"));
                    assertEquals("username2", properties.get("employee"));
                }
        );
    }

    @Test
    @Order(4)
    @SneakyThrows
    void updateActivity(){
        ACTIVITY_IO.setActivityName("changed");
        ACTIVITY_IO.setIndex(1);
        perform(put(PATH.replace("{adm}", "username1").replace("{emp}", "username2"))
                .contentType(JSON).content(OBJECT_MAPPER.writeValueAsString(ACTIVITY_IO)),
                status().isOk(),
                result -> {
                    StatusResponse statusResponse = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), StatusResponse.class);
                    assertEquals(Message.ACTIVITY_UPDATED, statusResponse.getMessage());
                }
        );
    }

    @Test
    @Order(5)
    @SneakyThrows
    void getUpdatedActivity(){
        perform(get(PATH.replace("{adm}", "username1").replace("{emp}", "username2"))
                        .contentType(JSON).content(OBJECT_MAPPER.writeValueAsString(ACTIVITY_IO)),
                status().isOk(),
                result -> {
                    JsonNode jsonNode = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), JsonNode.class);
                    List<ActivityIO> activityIOList = OBJECT_MAPPER.convertValue(jsonNode, new TypeReference<>() {
                    });
                    ActivityIO activityIO = activityIOList.get(0);
                    assertEquals("changed", activityIO.getActivityName());
                    assertEquals(1, activityIO.getIndex());
                }
        );
    }

    @Test
    @SneakyThrows
    @Order(6)
    void removeActivity(){
        perform(delete(PATH.replace("{adm}", "username1").replace("{emp}", "username2") + "/1"),
                status().isOk(),
                result -> {
                    StatusResponse statusResponse = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), StatusResponse.class);
                    assertEquals(Message.ACTIVITY_REMOVED_FROM_ADMIN, statusResponse.getMessage());
                }
        );

        perform(delete(PATH.replace("{adm}", "username1").replace("{emp}", "username2") + "/1"),
                status().isBadRequest(),
                result -> {
                    ExceptionResponse exceptionResponse = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), ExceptionResponse.class);
                    assertEquals(ApplicationExceptionHandler.INDEX_NOT_FOUND, exceptionResponse.getMessage());
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
