package com.kucess.notebook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.repo.EmployeeRepo;
import com.kucess.notebook.model.response.Message;
import com.kucess.notebook.model.response.StatusResponse;
import com.kucess.notebook.model.service.AdminService;
import com.kucess.notebook.model.service.EmployeeService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:employeeController")
@AutoConfigureMockMvc
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

    @BeforeEach
    @Transactional
    void insertData(){
        adminService.saveAdmin(ADMIN_IO);
    }

    @SneakyThrows
    @Test
    void employeeManagementTest(){

        // Add new employee to admin
        mockMvc.perform(post(ADMINS_PATH.replace("?", "KUCESS"))
                .contentType("application/json")
                .content(OBJECT_MAPPER.writeValueAsString(EMPLOYEE_IO2)))
                .andExpect(status().isOk())
                .andDo(result -> {
                    StatusResponse statusResponse = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), StatusResponse.class);
                    assertEquals("KUCESS" ,statusResponse.getProperties().get("admin"));
                    assertEquals("employee2" ,statusResponse.getProperties().get("employee"));
                });

        // Change employee password
        mockMvc.perform(post(EMPLOYEES_PATH + "/employee2")
                .param("pass", "12345"))
                .andExpect(status().isOk())
                .andDo(result -> {
                   StatusResponse statusResponse = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), StatusResponse.class);
                   assertEquals(Message.EMPLOYEE_PASSWORD_CHANGE,statusResponse.getMessage());
                   assertEquals("employee2" ,statusResponse.getProperties().get("employee"));
                });

        mockMvc.perform(get(EMPLOYEES_PATH + "/employee2"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    EmployeeIO employeeIO = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), EmployeeIO.class);
                    assertEquals("name2" , employeeIO.getName());
                });

        mockMvc.perform(delete(ADMINS_PATH.replace("?", "KUCESS") + "/employee2"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    StatusResponse response = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), StatusResponse.class);
                    assertEquals(Message.EMPLOYEE_REMOVED_FROM_ADMIN, response.getMessage());
                });

        employeeRepo.save(Employee.builder()
                        .name("name3")
                        .lastName("lastname3")
                        .userName("username3")
                .build());

        mockMvc.perform(put(ADMINS_PATH.replace("?", "KUCESS") + "/username3"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    StatusResponse statusResponse = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), StatusResponse.class);
                    assertEquals(Message.CURRENT_EMPLOYEE_ADD_TO_ADMIN ,statusResponse.getMessage());
                    Map<String, String> properties = statusResponse.getProperties();
                    assertEquals("KUCESS", properties.get("admin"));
                    assertEquals("username3", properties.get("employee"));
                });

    }


}
