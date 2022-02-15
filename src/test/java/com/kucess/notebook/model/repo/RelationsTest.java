package com.kucess.notebook.model.repo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.kucess.notebook.model.entity.Activity;
import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.Authority;
import com.kucess.notebook.model.entity.Employee;

@SpringBootTest
@ActiveProfiles("test")
public class RelationsTest {
	
	@Autowired
	EmployeeRepo employeeRepo;
	
	@Autowired
	AdminRepo adminRepo;
	
	@Autowired
	ActivityRepo activityRepo;
	
	
	@Test
	public void testRelations() {
		Admin admin = Admin.builder()
				.name("KUCESS")
				.lastName("KUCESS")
				.userName("KUCESS79")
				.password("1234")
				.build();
		
		Employee employee = Employee.builder()
				.name("AAA")
				.lastName("BBB")
				.userName("CCC")
				.password("1234")
				.build();
		
		
		admin.addAuthority(new Authority("ADM"));
		employee.addAuthority(new Authority("EMP"));
		
		admin.addEmployee(employee);
		
		adminRepo.save(admin);
		
		Activity activity = Activity.builder()
				.activityName("Test")
				.activityDescription("This is just a test")
				.score(10.5)
				.admin(admin)
				.employee(employee)
				.build();
		activityRepo.save(
				activity
		);
		
		Assertions.assertTrue(employeeRepo.findByUserName("CCC").isPresent());
		Assertions.assertEquals("EMP" ,employeeRepo.findByUserName("CCC").get().getAuthorities().get(0).getAuthorityName());
		Assertions.assertEquals(activity.getActivityName(), activityRepo.findById(activity.getId()).get().getActivityName());
		
	}
	

}
