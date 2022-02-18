package com.kucess.notebook.model.service;

import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.repo.EmployeeRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:test3")
class AdminServiceTest {

    @Autowired
    AdminService adminService;

    @Autowired
    EmployeeRepo employeeRepo;

    private static final AdminIO ADMIN_IO = AdminIO.builder()
            .name("kuc")
            .lastName("ces")
            .userName("test")
            .password("1234")
            .build();

    @BeforeEach
    void saveAdmin(){
        adminService.saveAdmin(ADMIN_IO);
    }

    @AfterEach
    void deleteAdmin(){
        adminService.deleteByUserName(ADMIN_IO.getUserName());
    }

    @Test
    @Transactional
    void readAdminTest(){
        AdminIO adminIO = adminService.findByUserName("test");
        assertEquals("ces" ,adminIO.getLastName());

        try {
            adminService.findByUserName("wrong");
            fail();
        }catch (Exception exception){
            assertTrue(exception instanceof IllegalArgumentException);
        }
    }

    @Test
    @Transactional
    void updateAdmin(){
        adminService.updateAdmin(ADMIN_IO.toBuilder().name("ser").build(), "test");
        assertEquals("ser" ,adminService.findByUserName("test").getName());
    }

    @Test
    @Transactional
    void addEmployeeToAdminTest(){
        adminService.addEmployeeToAdmin("test", EmployeeIO.builder().userName("emp").name("kucess").build());
        Optional<Employee> employeeOptional = employeeRepo.findByUserName("emp");
        assertTrue(employeeOptional.isPresent());
        assertEquals("kucess" ,employeeOptional.get().getName());

        AdminIO adminIO = adminService.findByUserName(ADMIN_IO.getUserName());
        assertEquals(1 ,adminIO.getEmployeeIOs().size());
    }

    @Test
    @Transactional
    void testEmployeeToAdminByUserNameTest(){
        Employee employee = Employee.builder()
                .name("sep")
                .userName("emp")
                .build();
        employeeRepo.save(employee);
        adminService.addEmployeeToAdmin("test", "emp");

        AdminIO adminIO = adminService.findByUserName("test");
        assertEquals("sep", adminIO.getEmployeeIOs().stream().findFirst()
                .get().getName());
    }

    @Test
    @Transactional
    void testRemoveEmployeeFromAdmin(){
        addEmployeeToAdminTest();
        adminService.removeEmployeeFromAdmin("test", "emp");
        assertEquals(0, adminService.findByUserName("test").getEmployeeIOs().size());
        assertNotNull(employeeRepo.findByUserName("emp"));
    }

}
