package com.project.film.domain;

// spring security에서 사용
public enum UserRole {
	USER("ROLE_USER"), ADMIND("ROLE_ADMIN");
	
	// field
	private String role;
	
	// constructor
	UserRole(String role) {
		this.role = role;
	}

	// getter
	public String getRole() {
		return this.role;
	}
}
