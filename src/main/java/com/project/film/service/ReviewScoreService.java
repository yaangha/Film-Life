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
	
	public Integer checkHeart(Integer reviewId, Integer usersId) {
		ReviewScore reviewScore = reviewScoreRepository.findHeart(reviewId, usersId);
		
		if (reviewScore == null) {
			return 0;
		} else {
			return reviewScore.getHeart();			
		}
	}

}
