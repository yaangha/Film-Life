package com.project.film.dto;

import java.time.LocalDateTime;

import com.project.film.domain.Reply;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplyReadDto {
	
	private Integer replyId;
	private Integer reviewId;
	private String replyText;
	private String writer;
	private LocalDateTime createdTime;
	private LocalDateTime modifiedTime;
	
	public static ReplyReadDto fromEntity(Reply entity) {
		return ReplyReadDto.builder()
				.replyId(entity.getId())
				.reviewId(entity.getReview().getId())
				.replyText(entity.getContent())
				.writer(entity.getUsers().getIdName())
				.createdTime(entity.getCreatedTime())
				.modifiedTime(entity.getModifiedTime()).build();
	}
}
