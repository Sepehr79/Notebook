package com.kucess.notebook.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
