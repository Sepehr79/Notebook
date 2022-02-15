package com.kucess.notebook.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class Activity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String activityName;
	
	private String activityDescription;
	
	private double score;
	
	@ManyToOne
	private Employee employee;
	
	@ManyToOne
	private Admin admin;

}
