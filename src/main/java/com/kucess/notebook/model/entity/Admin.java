package com.kucess.notebook.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ADM")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Getter
public class Admin extends Person {
	
	@OneToMany(mappedBy = "admin", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	private List<Activity> activities;


	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinTable(name = "admin_employee", joinColumns = @JoinColumn(name="employee_id"), inverseJoinColumns = @JoinColumn(name="admin_id"))
	private List<Employee> employees;


	public void addEmployee(Employee employee) {
		if (employee == null) {
			throw new IllegalArgumentException();
		}
		if (employees == null) {
			employees = new ArrayList<>();
		}
		employees.add(employee);
	}

	public void addActivity(Activity activity){
		if (activity == null)
			throw new IllegalArgumentException();
		if (activities == null)
			activities = new ArrayList<>();
		activities.add(activity);
	}
	
	

}
