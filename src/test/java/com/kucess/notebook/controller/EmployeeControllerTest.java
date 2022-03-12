package com.kucess.notebook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.repo.EmployeeRepo;
import com.kucess.notebook.model.response.ExceptionResponse;
import com.kucess.notebook.model.response.Message;
import com.kucess.notebook.model.response.StatusResponse;
import com.kucess.notebook.model.service.AdminService;
import com.kucess.notebook.model.service.EmployeeService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:employeeController"})
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class EmployeeControllerTest {

    private static final String EMPLOYEES_PATH = "/notebook/v1/employees";
    private static final String ADMINS_PATH = "/notebook/v1/admins/?/employees";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AdminService adminService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeRepo employeeRepo;

    private static final AdminIO ADMIN_IO = AdminIO.builder()
            .userName("KUCESS")
            .name("KUC")
            .lastName("ESS")
            .password("1234")
            .build();

    private static final EmployeeIO EMPLOYEE_IO1 = EmployeeIO.builder()
            .userName("employee1")
            .name("name1")
            .lastName("lastName1")
            .password("1234")
            .build();

    private static final EmployeeIO EMPLOYEE_IO2 = EmployeeIO.builder()
            .userName("employee2")
            .name("name2")
            .lastName("lastName2")
            .password("1234")
            .build();

    @BeforeAll
    @Transactional
    void insertData(){
        adminService.saveAdmin(ADMIN_IO);
    }

    @Test
    @Order(0)
    @SneakyThrows
    void addNewEmployee(){
        perform(post(ADMINS_PATH.replace("?", "KUCESS"))
                .contentType("application/json")
                .content(OBJECT_MAPPER.writeValueAsString(EMPLOYEE_IO2)),
                status().isOk(),
                result -> {
                    StatusResponse statusResponse = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), StatusResponse.class);
                    assertEquals("KUCESS" ,statusResponse.getProperties().get("admin"));
                    assertEquals("employee2" ,statusResponse.getProperties().get("employee"));
                });
    }

    @Test
    @Order(1)
    @SneakyThrows
    void duplicateEmployeeUsername(){
        perform(post(ADMINS_PATH.replace("?", "KUCESS"))
                .contentType("application/json")
                .content(OBJECT_MAPPER.writeValueAsString(EMPLOYEE_IO2.toBuilder().lastName("Another").build())),
                status().isBadRequest(),
                result -> {
                    ExceptionResponse exceptionResponse = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), ExceptionResponse.class);
                    assertEquals("Username already taken: employee2", exceptionResponse.getMessage());
                }
        );
    }

    @Test
    @Order(2)
    @SneakyThrows
    void changePassword(){
        perform(
                post(EMPLOYEES_PATH + "/employee2")
                        .param("pass", "12345"),
                status().isOk(),
                result -> {
                    StatusResponse statusResponse = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), StatusResponse.class);
                    assertEquals(Message.EMPLOYEE_PASSWORD_CHANGE,statusResponse.getMessage());
                    assertEquals("employee2" ,statusResponse.getProperties().get("employee"));
                }
        );
    }

    @Test
    @Order(3)
    @SneakyThrows
    void getEmployee(){
        perform(
                get(EMPLOYEES_PATH + "/employee2"),
                status().isOk(),
                result -> {
                    EmployeeIO employeeIO = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), EmployeeIO.class);
                    assertEquals("name2" , employeeIO.getName());
                }
        );
    }

    @Test
    @Order(4)
    @SneakyThrows
    void deleteEmployeeFromAdmin(){
        perform(
                delete(ADMINS_PATH.replace("?", "KUCESS") + "/employee2"),
                status().isOk(),
                result -> {
                    StatusResponse response = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), StatusResponse.class);
                    assertEquals(Message.EMPLOYEE_REMOVED_FROM_ADMIN, response.getMessage());
                }
        );

        assertTrue(employeeRepo.findByUserName("employee2").isPresent());
    }

    @Test
    @Order(5)
    @SneakyThrows
    void addCurrentEmployee(){
        employeeRepo.save(Employee.builder()
                .name("name3")
                .lastName("lastname3")
                .userName("username3")
                .build());

        perform(
                put(ADMINS_PATH.replace("?", "KUCESS") + "/username3"),
                status().isOk(),
                result -> {
                    StatusResponse statusResponse = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), StatusResponse.class);
                    assertEquals(Message.CURRENT_EMPLOYEE_ADD_TO_ADMIN ,statusResponse.getMessage());
                    Map<String, String> properties = statusResponse.getProperties();
                    assertEquals("KUCESS", properties.get("admin"));
                    assertEquals("username3", properties.get("employee"));
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
