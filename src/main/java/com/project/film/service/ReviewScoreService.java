package com.project.film.service;

import org.springframework.stereotype.Service;

import com.project.film.domain.ReviewScore;
import com.project.film.repository.ReviewScoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewScoreService {
	
	private final ReviewScoreRepository reviewScoreRepository;
	
	// detail.html에서 해당 리뷰 좋아요를 한 적이 있는지 체크 
	public Integer checkHeart(Integer reviewId, Integer usersId) {
		ReviewScore reviewScore = reviewScoreRepository.findScore(reviewId, usersId);
		
		if (reviewScore == null) {
			return 0;
		} else {
			return reviewScore.getHeart();			
		}
	}

}
