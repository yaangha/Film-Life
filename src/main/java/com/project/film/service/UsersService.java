package com.project.film.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.film.domain.Image;
import com.project.film.domain.Reply;
import com.project.film.domain.Review;
import com.project.film.domain.ReviewScore;
import com.project.film.domain.Users;
import com.project.film.repository.ImageRepository;
import com.project.film.repository.ReplyRepository;
import com.project.film.repository.ReviewRepository;
import com.project.film.repository.ReviewScoreRepository;
import com.project.film.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UsersService {
	
	private final PasswordEncoder passwordEncoder;
	private final UsersRepository userRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewScoreRepository reviewScoreRepository;
	private final ReplyRepository replyRepository;
	private final ImageRepository imageRepository;
	
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
		// 비밀번호 암호화한 후 데이터베이스에 저장
		fromEntity.setPassword(passwordEncoder.encode(fromEntity.getPassword()));
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
		log.info("haeun.. plz...{}, {}", idName, user.getIdName());
		if (passwordEncoder.matches(password, user.getPassword())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * IdName으로 사용자 찾을 때 사용
	 * @param idName
	 * @return
	 */
	public Users read(String idName) {
		Users user = userRepository.findByIdName(idName).get();
		return user;
	}

	public void deleteUser(String idName) {
		Users user = userRepository.findByIdName(idName).get();
		List<ReviewScore> rs = reviewScoreRepository.findByUsersId(user.getId());
		for (ReviewScore r : rs) {
			reviewScoreRepository.delete(r);
		}
		
		List<Reply> replies = replyRepository.findByUsersId(user.getId());
		for (Reply r : replies) {
			replyRepository.delete(r);
		}
		
		List<Review> reviews = reviewRepository.findByAuthorOrderByIdDesc(idName);
		for (Review r : reviews) {
			List<Image> imageList = imageRepository.findByReviewId(r.getId());
			for (Image i : imageList) {
				imageRepository.delete(i);
			}
			reviewRepository.delete(r);
		}
		
		userRepository.delete(user);
				
	}
}
