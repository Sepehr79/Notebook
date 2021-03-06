package com.kucess.notebook.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor
@Getter
@SuperBuilder(toBuilder = true)
@Setter
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String name;
	
	private String lastName;
	
	@Column(unique = true)
	private String userName;
	
	private String password;

	@Enumerated(EnumType.STRING)
	private AuthorityType authorityType;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Activity> activities;

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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Person person = (Person) o;
		return id != 0 && Objects.equals(id, person.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
