package com.project.film.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = {"review", "users"})
@Entity(name = "REVIEWSCORE")
@SequenceGenerator(name = "REVIEWSCORE_SEQ_GEN", sequenceName = "REVIEWSCORE_SEQ", allocationSize = 1)
public class ReviewScore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REVIEWSCORE_SEQ_GEN")
	private Integer id;
	
	@ManyToOne
	private Users users; // 외래키 연결(user_id)
	
	@ManyToOne
	private Review review; // 외래키 연결(review_id)
	
	private Integer heart; // 좋아요
	
	private Integer watch; // 조회수
}
