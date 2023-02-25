package com.project.film.dto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.project.film.domain.Users;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserSecurityDto extends User {
	// 로그인할 때 필요한 정보 & 로그인 후에 뷰에서 필요한 정보
	private String idName;
	private String password;
	private String username;
	
	public UserSecurityDto(String idName, String password, String username, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		
		this.idName = idName;
		this.password = password;
		this.username = username;
	}
	
	public static UserSecurityDto formEntity(Users u) {
		List<GrantedAuthority> authorities = u.getRoles().stream()
				.map(x -> new SimpleGrantedAuthority(x.getRole()))
				.collect(Collectors.toList());
		UserSecurityDto dto = new UserSecurityDto(u.getIdName(), u.getPassword(), u.getUsername(), authorities);
		
		return dto;
	}

}
