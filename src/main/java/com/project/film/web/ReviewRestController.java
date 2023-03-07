package com.project.film.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.film.dto.UserSecurityDto;
import com.project.film.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReviewRestController {
	
	private final ReviewService reviewService;
	
	@PostMapping("/api/review/heart")
	public ResponseEntity<Integer> addHeart(@AuthenticationPrincipal UserSecurityDto userSecurityDto, Integer reviewId) {
		log.info("hauen Integer reviewId = {}", reviewId);
		Integer result = reviewService.addHeart(reviewId, userSecurityDto.getIdName());
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/api/review/heartDelete")
	public ResponseEntity<Integer> deleteHeart(@AuthenticationPrincipal UserSecurityDto userSecurityDto, Integer reviewId) {
		Integer result = reviewService.deleteHeart(reviewId, userSecurityDto.getIdName());
		return ResponseEntity.ok(result);
	}
}
