package com.kucess.notebook.model.repo;

import com.kucess.notebook.model.entity.Activity;
import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.AuthorityType;
import com.kucess.notebook.model.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:test1")
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
			.authorityType(AuthorityType.ADMIN)
			.build();

	private static Employee employee = Employee.builder()
			.name("AAA")
			.lastName("BBB")
			.userName("CCC")
			.password("1234")
			.authorityType(AuthorityType.EMPLOYEE)
			.build();

	private static Activity activity;

	@BeforeAll
	void configRelations(){
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
		assertEquals("EMPLOYEE" ,employeeRepo.findByUserName("CCC").get().getAuthorityType().name());
	}

	@Test
	void testActivityRelations(){
		assertEquals(activity.getActivityName(), activityRepo.findById(activity.getId()).get().getActivityName());
	}

	@Test
	void testChangeUserName(){
		assertTrue(adminRepo.findByUserName("KUCESS79").isPresent());
		adminRepo.save(admin.toBuilder().userName("test").build());
		assertFalse(adminRepo.findByUserName("KUCESS79").isPresent());
		assertTrue(adminRepo.findByUserName("test").isPresent());
	}

	

}
