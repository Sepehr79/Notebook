package com.kucess.notebook.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("ADM")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Getter
@Setter
public class Admin extends Person {
	
	@OneToMany(mappedBy = "admin", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	private List<Activity> activities;


	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinTable(name = "admin_employee", joinColumns = @JoinColumn(name="employee_id"), inverseJoinColumns = @JoinColumn(name="admin_id"))
	private Set<Employee> employees;


	public void addEmployee(Employee employee) {
		if (employee == null) {
			throw new IllegalArgumentException();
		}
		if (employees == null) {
			employees = new HashSet<>();
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
