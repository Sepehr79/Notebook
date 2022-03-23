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
@DiscriminatorValue("EMP")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Getter
@Setter
public class Employee extends Person {

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Activity> activities;
	
	
	@ManyToMany(mappedBy = "employees", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private Set<Admin> admins;
	
	
	public void addAdmin(Admin admin) {
		if (admin == null) {
			throw new IllegalArgumentException();
		}
		if (admins == null) {
			admins = new HashSet<>();
		}
		admins.add(admin);
	}

	public void addActivity(Activity activity){
		if (activity == null){
			throw new IllegalArgumentException();
		}
		if (activities == null){
			activities = new ArrayList<>();
		}
		activities.add(activity);
	}

	@Override
	public boolean equals(Object o){
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
