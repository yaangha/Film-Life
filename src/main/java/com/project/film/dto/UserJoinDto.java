package com.project.film.dto;

import com.project.film.domain.Users;

import lombok.Data;

@Data
public class UserJoinDto {

	private String idName;
	private String username;
	private String password;
	private String email;
	private String phone;
	
	public Users fromEntity() {
		return Users.builder().idName(idName).username(username).password(password).email(email).phone(phone).build();
	}
}
