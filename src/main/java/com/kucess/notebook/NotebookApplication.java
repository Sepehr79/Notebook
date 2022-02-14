package com.kucess.notebook;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.Authority;
import com.kucess.notebook.model.entity.Employee;
import com.kucess.notebook.model.repo.AdminRepo;
import com.kucess.notebook.model.repo.EmployeeRepo;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class NotebookApplication implements CommandLineRunner {

	private final AdminRepo adminRepo;
	
	private final EmployeeRepo employeeRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(NotebookApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Admin admin = Admin.builder()
				.name("sepehr")
				.lastName("Mollaei")
				.userName("sepehr79")
				.password("1234")
				.build();
		
		admin.addAuthority(new Authority("ADMIN"));
		
		Employee employee = Employee.builder()
				.name("sepehr")
				.lastName("mollaei")
				.userName("a")
				.password("1234")
				.build();
		
		employee.addAuthority(new Authority("EMP"));
		
		adminRepo.save(admin);
		employeeRepo.save(employee);
	}

}
