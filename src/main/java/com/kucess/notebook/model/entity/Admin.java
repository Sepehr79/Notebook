package com.kucess.notebook.model.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
public class Admin extends Person {
	
	@OneToMany(mappedBy = "admin")
	private List<Activity> activities;
	
	
	@OneToMany(mappedBy = "admins")
	private List<Employee> employees;
	
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
	@JoinTable(name = "admin_authority")
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
