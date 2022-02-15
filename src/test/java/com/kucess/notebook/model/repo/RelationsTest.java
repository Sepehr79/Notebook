package com.kucess.notebook.model.repo;

import com.kucess.notebook.model.entity.Activity;
import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.Authority;
import com.kucess.notebook.model.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RelationsTest {
	
	@Autowired
	EmployeeRepo employeeRepo;
	
	@Autowired
	AdminRepo adminRepo;
	
	@Autowired
	ActivityRepo activityRepo;

	private static Admin admin = Admin.builder()
			.name("KUCESS")
			.lastName("KUCESS")
			.userName("KUCESS79")
			.password("1234")
			.build();

	private static Employee employee = Employee.builder()
			.name("AAA")
			.lastName("BBB")
			.userName("CCC")
			.password("1234")
			.build();

	private static Activity activity;

	@BeforeAll
	void configRelations(){
		admin.addAuthority(new Authority("ADM"));
		employee.addAuthority(new Authority("EMP"));
		admin.addEmployee(employee);
		activity = Activity.builder()
				.activityName("Test")
				.activityDescription("This is just a test")
				.score(10.5)
				.admin(admin)
				.employee(employee)
				.build();

		adminRepo.save(admin);
		activityRepo.save(activity);

	}
	
	
	@Test
	void testEmployeeRelations() {
		Assertions.assertTrue(employeeRepo.findByUserName("CCC").isPresent());
		Assertions.assertEquals("EMP" ,employeeRepo.findByUserName("CCC").get().getAuthorities().get(0).getAuthorityName());
	}

	@Test
	void testActivityRelations(){
		Assertions.assertEquals(activity.getActivityName(), activityRepo.findById(activity.getId()).get().getActivityName());
	}
	

}
