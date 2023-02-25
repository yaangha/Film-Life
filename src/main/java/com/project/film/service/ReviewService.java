package com.project.film.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.film.domain.Review;
import com.project.film.dto.ReviewCreateDto;
import com.project.film.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
	
	private final ReviewRepository reviewRepository;
	
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

}
