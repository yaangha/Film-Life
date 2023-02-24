package com.project.film.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.film.domain.Users;
import com.project.film.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UsersService {
	
	private final UsersRepository userRepository;
	
	/**
	 * idName 중복체크
	 * @param idName
	 * @return
	 */
	public String checkIdName(String idName) {
		Optional<Users> result = userRepository.findByIdName(idName);
		if (result.isPresent()) {
			return "nok";
		} else {
			return "ok";
		}
		
	}
	
	/**
	 * 회원가입 정보
	 * @param fromEntity
	 * @return
	 */
	public Users create(Users fromEntity) {
		Users user = userRepository.save(fromEntity);
		return user;
	}

	/**
	 * 로그인 정보 확인 
	 * @param idName
	 * @param password
	 * @return
	 */
	public Boolean checkLogin(String idName, String password) {
		Users user = userRepository.findByIdName(idName).get();
		if (user.getPassword().equals(password)) {
			return true;
		} else {
			return false;
		}
	}
}
