package com.project.film.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.film.domain.Review;

import lombok.Data;

@Data
public class ReviewCreateDto {
	
	private Integer reviewId;
	private String author;
	private String title;
	private String content;
	private Integer score;
	private Integer storage;
	
	private List<MultipartFile> files; 
	
	public Review toEntity() {
		return Review.builder()
				.author(author).title(title).content(content).score(score).storage(storage)
				.build();
	}
	
}
