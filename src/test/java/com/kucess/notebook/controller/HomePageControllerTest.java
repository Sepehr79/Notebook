package com.kucess.notebook.controller;

import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.AuthorityType;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class HomePageControllerTest {

    @InjectMocks
    HomePageController homePageController;

    @Mock
    EmployeeService employeeService;

    @Test
    void findUserTest() {
        mockEmpService();
        Authentication authentication = Mockito.mock(Authentication.class);
        Model model = new BindingAwareModelMap();
        Mockito.when(authentication.getName()).thenReturn("admin");
        homePageController.findUser(authentication, model);
        assertTrue(model.containsAttribute("employees"));
        Object object = model.getAttribute("user");
        assertTrue(object instanceof Admin);

        Mockito.when(authentication.getName()).thenReturn("employee");
        homePageController.findUser(authentication, model);
        assertTrue(model.containsAttribute("activities"));
    }

    @Test
    void addEmployeePageTest(){
        Model model = new BindingAwareModelMap();
        homePageController.addEmployeesPage(model);
        assertTrue(model.getAttribute("employee") instanceof EmployeeIO);
    }


    private void mockEmpService(){
        Mockito.when(employeeService.getUserByUserName("admin"))
                .thenReturn(
                        Admin.builder()
                                .name("admin")
                                .lastName("admin")
                                .userName("admin")
                                .password("admin")
                                .authorityType(AuthorityType.ADMIN)
                                .build()
                );
        Mockito.when(employeeService.getUserByUserName("employee"))
                .thenReturn(
                        Employee.builder()
                                .name("employee")
                                .lastName("employee")
                                .userName("employee")
                                .password("employee")
                                .authorityType(AuthorityType.EMPLOYEE)
                                .build()
                );
    }

}
