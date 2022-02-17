package com.kucess.notebook.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor
@Getter
@SuperBuilder(toBuilder = true)
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
}
