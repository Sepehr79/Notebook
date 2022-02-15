package com.kucess.notebook.model.entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("EMP")
@SuperBuilder
@NoArgsConstructor
public class Employee extends Person {

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
	private List<Activity> activities;
	
	
	@ManyToMany(mappedBy = "employees")
	private List<Admin> admins;
	
	
	public void addAdmin(Admin admin) {
		if (admin == null) {
			throw new IllegalArgumentException();
		}
		if (admins == null) {
			admins = new ArrayList<Admin>();
		}
		admins.add(admin);
	}
	
	
}
