package com.kucess.notebook.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
	private List<Admin> admins;
	
	
	public void addAdmin(Admin admin) {
		if (admin == null) {
			throw new IllegalArgumentException();
		}
		if (admins == null) {
			admins = new ArrayList<>();
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

}
