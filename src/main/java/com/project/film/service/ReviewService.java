package com.project.film.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.film.domain.Review;
import com.project.film.domain.ReviewScore;
import com.project.film.domain.Users;
import com.project.film.dto.ReviewCreateDto;
import com.project.film.dto.ReviewReadDto;
import com.project.film.repository.ReviewRepository;
import com.project.film.repository.ReviewScoreRepository;
import com.project.film.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
	
	private final ReviewRepository reviewRepository;
	private final ReviewScoreRepository reviewScoreRepository;
	private final UsersRepository usersRepository;
	
	/**
	 * create 창에서 데이터 저장
	 * @param dto 테이블에 저장할 데이터
	 * @return 객체 리턴
	 */
	public Review create(ReviewCreateDto dto) {
		
		// dto.setStorage(0);
		Review entity = reviewRepository.save(dto.toEntity());
		
		return entity;
	}
	
	/**
	 * detail에서 보여줄 데이터 찾는 메소드
	 * @param reviewId review table PK
	 * @return 
	 */
	public Review read(Integer reviewId) {
		return reviewRepository.findById(reviewId).get();
	}

	public List<Review> readAll() {
		return reviewRepository.findByOrderByIdDesc();
	}
	
	public List<Review> readRelease() {
		List<Review> listAll = readAll();
		List<Review> list = new ArrayList<>();
		for (Review r : listAll) {
			if (r.getStorage() == 1) {
				list.add(r);
			}
		}
		return list;
	}

	public List<ReviewReadDto> readReleaseAll() {
		List<Review> listAll = readAll();
		List<ReviewReadDto> list = new ArrayList<>();
		for (Review r : listAll) {
			if (r.getStorage() == 1) {
				List<ReviewScore> reviewScore = reviewScoreRepository.findByReviewId(r.getId());
				Integer heart = 0;
				for (ReviewScore rs : reviewScore) {
					heart += rs.getHeart();
				}
				list.add(ReviewReadDto.fromEntity(r, heart, 0));
			}
		}
		return list;
	}
	
	public ReviewReadDto readReview(Integer reviewId) {
		ReviewReadDto reviewDto = null;
		
		Review review = reviewRepository.findById(reviewId).get();
		
		List<ReviewScore> reviewScoreList = reviewScoreRepository.findByReviewId(reviewId);
		Integer heart = 0;
		for (ReviewScore rs : reviewScoreList) {
			heart += rs.getHeart();
		}
		
		// TODO watch 
		
		reviewDto = ReviewReadDto.fromEntity(review, heart, 0);
		
		return reviewDto;
	}

	public Integer addHeart(Integer reviewId, String idName) {
		Integer result = 0;
		Users user = usersRepository.findByIdName(idName).get();
		Review review = reviewRepository.findById(reviewId).get();
		ReviewScore rs = reviewScoreRepository.findHeart(reviewId, user.getId());

		if (rs == null) { // 사용자 데이터가 없을 경우에는 create
			rs = ReviewScore.builder().users(user).review(review).heart(1).build();
			reviewScoreRepository.save(rs);
			result = 0;
		} else { // 사용자 데이터가 있을 경우에는 update
			rs.setHeart(1);
			reviewScoreRepository.save(rs);
			result = 1;
		}
		
		return result;
	}

	public Integer deleteHeart(Integer reviewId, String idName) {
		Users user = usersRepository.findByIdName(idName).get();
		ReviewScore rs = reviewScoreRepository.findHeart(reviewId, user.getId());
		rs.setHeart(0);
		reviewScoreRepository.save(rs);	
		return rs.getHeart();
	}

	

}
