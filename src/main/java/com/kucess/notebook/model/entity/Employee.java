package com.kucess.notebook.model.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
public class Employee extends Person {

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
	private List<Activity> activities;
	
	
	@ManyToMany
	@JoinTable(name = "admin_employee", joinColumns = @JoinColumn(name="employee_id"), inverseJoinColumns = @JoinColumn(name="admin_id"))
	private List<Admin> admins;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
	@JoinTable(name = "employee_authority")
	private Set<Authority> authorities;
	
	public void addAuthority(Authority authority) {
		if (authority == null) {
			throw new IllegalArgumentException();
		}
		if (authorities == null) {
			authorities = new HashSet<Authority>();
		}
		authorities.add(authority);
		
	}
	
}
