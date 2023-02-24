package com.project.film.dto;

import com.project.film.domain.Review;

import lombok.Data;

@Data
public class ReviewCreateDto {
	
	private String author;
	private String title;
	private String content;
	// private String image; // 추후 사용,,
	private Integer score;
	private Integer storage;
	
	public Review toEntity() {
		return Review.builder()
				.author(author).title(title).content(content).score(score).storage(storage)
				.build();
	}
	
}
