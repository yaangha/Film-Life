package com.project.film.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity(name = "USERS") // table name
@SequenceGenerator(name = "USERS_SEQ_GEN", sequenceName = "USERS_SEQ", allocationSize = 1)
public class Users extends BaseTimeEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ_GEN")
	private Integer id;	// PK
	
	@Column(unique = true, nullable = false)
	private String idName; // 사용자 아이디
	
	@Column(nullable = false)
	private String username; // 사용자 이름
	
	private String phone; // 전화번호
	
	private String password; // 비밀번호
	
	private String email;
	
	private String address; // 주소
	
	private String dormantChk; // 휴면여부
	
	private String withdrawChk; // 탈퇴여부
	
}
