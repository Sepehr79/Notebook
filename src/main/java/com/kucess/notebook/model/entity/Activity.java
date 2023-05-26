package com.kucess.notebook.model.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
@Setter
public class Activity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private @NonNull String activityName;
	
	private @NonNull String activityDescription;
	
	private @NonNull double score;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(nullable = false)
	private Employee employee;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(nullable = false)
	private Admin admin;

	@CreationTimestamp
	private Date date;

}
