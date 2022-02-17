package com.kucess.notebook.model.service;

import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.repo.EmployeeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:test3")
class EmployeeServiceTest {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeRepo employeeRepo;

    private static final Employee EMPLOYEE = Employee.builder()
            .name("kuc")
            .lastName("cess")
            .userName("kucess")
            .password("1234")
            .build();

    @BeforeEach
    void add(){
        employeeRepo.save(EMPLOYEE);
    }

    @Test
    @Transactional
    void testGetEmployeeAndChangePassword(){
        EmployeeIO employee = employeeService.findByUserName("kucess");
        assertEquals("kuc" ,employee.getName());

        employeeService.changePassword("kucess", "change");
        assertEquals("change" ,employeeRepo.findByUserName("kucess").get().getPassword());
    }

}
