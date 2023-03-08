package com.project.film.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.film.domain.Review;
import com.project.film.domain.Users;
import com.project.film.dto.ReviewCreateDto;
import com.project.film.dto.ReviewReadDto;
import com.project.film.dto.UserSecurityDto;
import com.project.film.repository.ReviewScoreRepository;
import com.project.film.service.ReviewScoreService;
import com.project.film.service.ReviewService;
import com.project.film.service.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewController {
	
	private final ReviewService reviewService; // @RequiredArgsConstructor 어노테이션 필요 -> final 필드를 초기화해줌
	private final UsersService userService;
	private final ReviewScoreService reviewScoreService;
	
	@GetMapping("/main")
	public void main(Model model) {
		List<ReviewReadDto> reviewAll = reviewService.readReleaseAll();
		model.addAttribute("reviewAll", reviewAll);
		
	}
	
	@GetMapping("/create")
	public void create() {		
	}
	
	@PostMapping("/create")
	public String create(ReviewCreateDto dto) {
		log.info("here??????");
		Review entity = reviewService.create(dto);
		if (entity.getStorage() == 0) {
			return "redirect:/review/main";
		} else {
			return "redirect:/review/detail?reviewId=" + entity.getId();
		}
	}
	
	@GetMapping("/detail")
	public void detail(@AuthenticationPrincipal UserSecurityDto userSecurityDto, Integer reviewId, Model model) {
		Review review = reviewService.read(reviewId);
		if (userSecurityDto != null) {
			Users user = userService.read(userSecurityDto.getIdName());
			Integer heart = reviewScoreService.checkHeart(review.getId(), user.getId());
			log.info("heart,,, {}", heart);		
			model.addAttribute("heart", heart);			
		}
		
		ReviewReadDto reviewDto = reviewService.readReview(reviewId);
		model.addAttribute("reviewDto",reviewDto);
		model.addAttribute("review", review);
		
		List<ReviewReadDto> reviewAll = reviewService.readReleaseAll();
		List<ReviewReadDto> otherReview = new ArrayList<>();
		
		for (ReviewReadDto dto : reviewAll) {
			if (dto.getReviewId() != reviewId) {
				otherReview.add(dto);
			}
		}
		
		model.addAttribute("otherReview", otherReview);
	}
	
	@GetMapping("/modify")
	public void modify(Integer reviewId, Model model) {
		Review review = reviewService.read(reviewId);
		model.addAttribute("review", review);
	}
	
	@PostMapping("/modify")
	public String modify(ReviewCreateDto dto) {
		Integer reviewId = reviewService.modify(dto);
		return "redirect:/review/detail?reviewId=" + reviewId;
	}
	
	@PostMapping("/search")
	public String search(String type, String keyword, Model model) {
		List<ReviewReadDto> reviewAll = reviewService.search(type, keyword); 
		model.addAttribute("reviewAll",reviewAll);
		return "/review/main";
	}
	
	@PostMapping("/delete")
	public String delete(Integer reviewId) {
		reviewService.delete(reviewId);
		return "redirect:/review/main";
	}
	
}
