package com.kucess.notebook;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kucess.notebook.model.entity.AuthorityType;
import com.kucess.notebook.model.entity.Person;
import com.kucess.notebook.model.io.ActivityIO;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.io.EmployeeIO;
import com.kucess.notebook.model.repo.PersonRepo;
import com.kucess.notebook.model.response.Message;
import com.kucess.notebook.model.response.StatusResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.datasource.url=jdbc:h2:mem:integ")
class NotebookApplicationTests {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@LocalServerPort
	int port;

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	PersonRepo personRepo;

	private static final AdminIO ADMIN_IO1 = AdminIO.builder()
			.name("name1")
			.lastName("lastname1")
			.password("1234")
			.userName("admin1")
			.build();

	private static final AdminIO ADMIN_IO2 = AdminIO.builder()
			.name("name2")
			.lastName("lastname2")
			.password("1234")
			.userName("admin2")
			.build();

	private static final EmployeeIO EMPLOYEE_IO1 = EmployeeIO.builder()
			.name("name3")
			.lastName("lastname3")
			.password("1234")
			.userName("employee1")
			.build();

	private static final EmployeeIO EMPLOYEE_IO2 = EmployeeIO.builder()
			.name("name3")
			.lastName("lastname3")
			.password("1234")
			.userName("employee2")
			.build();

	private static final EmployeeIO EMPLOYEE_IO3 = EmployeeIO.builder()
			.name("name3")
			.lastName("lastname3")
			.password("1234")
			.userName("employee3")
			.build();

	private static final ActivityIO ACTIVITY_IO1 = new ActivityIO("test1", "this is test1", 10);
	private static final ActivityIO ACTIVITY_IO2 = new ActivityIO("test2", "this is test2", 20);
	private static final ActivityIO ACTIVITY_IO3 = new ActivityIO("test3", "this is test3", 30);


	@Test
	void contextLoads() {
		String url = "http://localhost:" + port + "/notebook/v1/admins";
		StatusResponse statusResponse1 =
				testRestTemplate.postForObject(url, ADMIN_IO1, StatusResponse.class);
		assertEquals(Message.ADMIN_REGISTERED, statusResponse1.getMessage());

		assertTrue(personRepo.findPersonByUserName(ADMIN_IO1.getUserName()).isPresent());

		testRestTemplate.postForObject(url, ADMIN_IO2, StatusResponse.class);

		StatusResponse statusResponse2 = testRestTemplate.postForObject(url + "/admin1/employees", EMPLOYEE_IO1, StatusResponse.class);
		assertEquals(Message.EMPLOYEE_ADD_TOO_ADMIN ,statusResponse2.getMessage());

		testRestTemplate.postForObject(url + "/admin2/employees", EMPLOYEE_IO2, StatusResponse.class);
		testRestTemplate.postForObject(url + "/admin2/employees", EMPLOYEE_IO3, StatusResponse.class);

		Person person1 = personRepo.findPersonByUserName(ADMIN_IO1.getUserName()).get();
		assertEquals(AuthorityType.ADMIN ,person1.getAuthorityType());

		Person person2 = personRepo.findPersonByUserName(EMPLOYEE_IO2.getUserName()).get();
		assertEquals(AuthorityType.EMPLOYEE, person2.getAuthorityType());

		assertTrue(personRepo.findPersonByUserName(EMPLOYEE_IO2.getUserName()).isPresent());
		assertTrue(personRepo.findPersonByUserName(EMPLOYEE_IO3.getUserName()).isPresent());

		StatusResponse statusResponse3 = testRestTemplate.postForObject(url + "/admin1/employees/employee1/activities", ACTIVITY_IO1, StatusResponse.class);
		assertEquals(Message.ACTIVITY_ADDED_TO_EMPLOYEE ,statusResponse3.getMessage());

		testRestTemplate.postForObject(url + "/admin1/employees/employee2/activities", ACTIVITY_IO2, StatusResponse.class);
		testRestTemplate.postForObject(url + "/admin1/employees/employee2/activities", ACTIVITY_IO3, StatusResponse.class);

		JsonNode jsonNode1 = testRestTemplate.getForObject(url + "/admin1/employees/employee2/activities", JsonNode.class);
		List<ActivityIO> activityIOList1 = OBJECT_MAPPER.convertValue(jsonNode1, new TypeReference<List<ActivityIO>>() {});
		assertEquals(2 ,activityIOList1.size());

		testRestTemplate.delete(url + "/admin1/employees/employee2/activities/2");

		List<ActivityIO> activityIOList2 = testRestTemplate.getForObject(url + "/admin1/employees/employee2/activities", List.class);
		assertEquals(1 ,activityIOList2.size());

		ACTIVITY_IO2.setActivityName("updated");
		ACTIVITY_IO2.setIndex(1);
		testRestTemplate.put(url + "/admin1/employees/employee2/activities", ACTIVITY_IO2);

		JsonNode jsonNode3 = testRestTemplate.getForObject(url + "/admin1/employees/employee2/activities", JsonNode.class);
		List<ActivityIO> activityIOList = OBJECT_MAPPER.convertValue(jsonNode3, new TypeReference<>() {});
		ActivityIO updated = activityIOList.stream().findFirst().get();
		assertEquals("updated" ,updated.getActivityName());
	}

}
