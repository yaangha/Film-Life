package com.project.film.dto;

import java.time.LocalDateTime;

import com.project.film.domain.Review;
import com.project.film.domain.ReviewScore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewReadDto {
	private Integer reviewId;
	private String title;
	private String content;
	private String idName;
	private Integer heart;
	private Integer watch;
	private Integer replyCount;
	private LocalDateTime createdTime;
	private LocalDateTime modifiedTime;
	
	public static ReviewReadDto fromEntity(Review review, Integer heart, Integer watch, Integer replyCount) {
		return ReviewReadDto.builder()
				.reviewId(review.getId()).title(review.getTitle()).content(review.getContent()).idName(review.getAuthor())
				.heart(heart).watch(watch).createdTime(review.getCreatedTime()).modifiedTime(review.getModifiedTime()).replyCount(replyCount)
				.build();
	}
}
