package com.project.film.dto;

import lombok.Data;

@Data
public class ReplyRegisterDto {
	private Integer reviewId;
	private String replyText;
	private String writer;
}
