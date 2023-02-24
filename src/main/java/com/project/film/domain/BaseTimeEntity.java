package com.project.film.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass // 다른 entity 클래스의 상위 클래스
@EntityListeners(value = { AuditingEntityListener.class })
public class BaseTimeEntity {
	
	@CreatedDate
	private LocalDateTime createdTime;
	
	@LastModifiedDate
	private LocalDateTime modifiedTime;
	
}
