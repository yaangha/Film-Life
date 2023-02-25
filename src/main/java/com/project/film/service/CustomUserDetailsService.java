package com.project.film.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.film.domain.Users;
import com.project.film.dto.UserSecurityDto;
import com.project.film.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	private final UsersRepository usersRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		Optional<Users> entity = usersRepository.findByIdName(username);
		if (entity.isPresent()) {
			return UserSecurityDto.formEntity(entity.get());
		} else {
			throw new UsernameNotFoundException(username + ": not found!");
		}
	}
	
}
