package com.kucess.notebook.model.service;

import com.kucess.notebook.model.io.AdminIO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:test3")
class AdminServiceTest {

    @Autowired
    AdminService adminService;

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
    }

    @Test
    @Transactional
    void updateAdmin(){
        adminService.updateAdmin(ADMIN_IO.toBuilder().name("ser").build(), "test");
        assertEquals("ser" ,adminService.findByUserName("test").getName());
    }

}
