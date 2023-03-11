package com.project.film.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity(name = "IMAGE")
@SequenceGenerator(name = "IMAGE_SEQ_GEN", sequenceName = "IMAGE_SEQ", allocationSize = 1)
public class Image {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMAGE_SEQ_GEN")
	private Long id;
	
	@Column(nullable = false)
	private String originName; // 기존 파일명
	
	@Column(nullable = false)
	private String fileName; // 서버 파일명
	
	@Column(nullable = false)
	private String filePath;
	
	@ManyToOne
	private Review review;
}
