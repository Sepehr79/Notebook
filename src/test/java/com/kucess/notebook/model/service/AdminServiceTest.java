package com.kucess.notebook.model.service;

import com.kucess.notebook.model.entity.Activity;
import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.ActivityIO;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.repo.ActivityRepo;
import com.kucess.notebook.model.repo.AdminRepo;
import com.kucess.notebook.model.repo.EmployeeRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:test3")
class AdminServiceTest {

    @Autowired
    AdminService adminService;

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    ActivityRepo activityRepo;

    @Autowired
    AdminRepo adminRepo;

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

    @Test
    @Transactional
    void testAddActivityToEmployee(){
        addEmployeeToAdminTest();
        adminService.addActivityToEmployee("test", "emp", ActivityIO.builder()
                        .activityName("test")
                        .activityDescription("This is just a test")
                        .score(10)
                .build());

        Employee employee = employeeRepo.findByUserName("emp").get();
        assertEquals(1 ,employee.getActivities().size());
        Activity activity = employee.getActivities().stream().findFirst().get();
        assertEquals("test" ,activity.getAdmin().getUserName());
        assertEquals("emp", activity.getEmployee().getUserName());
        Admin admin = adminRepo.findByUserName("test").get();
        assertEquals(1, admin.getActivities().size());

        List<ActivityIO> activities = adminService.findByAdminUserNameAndEmployeeUserName("test", "emp");
        ActivityIO activityIO = activities.stream().findFirst().get();
        assertEquals(1, activities.size());
        assertEquals(activity.getActivityName(), activityIO.getActivityName());
        assertEquals(1 ,activityIO.getIndex());

        activityIO.setScore(20);
        adminService.updateActivityFromEmployee("test", "emp", activityIO);
        assertEquals(20 ,employeeRepo.findByUserName("emp").get().getActivities().stream().findFirst().get().getScore());


        adminService.deleteActivityFromEmployee("test", "emp", 1);
        assertEquals(0 ,employeeRepo.findByUserName("emp").get().getActivities().size());
        assertFalse(activityRepo.findAll().iterator().hasNext());
        assertEquals(0, adminRepo.findByUserName("test").get().getActivities().size());
    }

}
