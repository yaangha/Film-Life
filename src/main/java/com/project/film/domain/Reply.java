package com.project.film.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString(exclude = {"review", "users"})
@Entity(name = "REPLY")
@SequenceGenerator(name = "REPLY_SEQ_GEN", sequenceName = "REPLY_SEQ", allocationSize = 1)
public class Reply extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPLY_SEQ_GEN")
	private Integer id;
	
	@ManyToOne
	private Users users; // 외래키 연결(user_id)
	
	@ManyToOne
	private Review review; // 외래키 연결(review_id)
	
	@Column(nullable = false)
	private String content; // 댓글 내용
	
}
