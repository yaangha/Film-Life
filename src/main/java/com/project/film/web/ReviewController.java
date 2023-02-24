package com.project.film.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.film.domain.Review;
import com.project.film.dto.ReviewCreateDto;
import com.project.film.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewController {
	
	private final ReviewService reviewService; // @RequiredArgsConstructor 어노테이션 필요 -> final 필드를 초기화해줌
	
	@GetMapping("/main")
	public void main() {
	
	}
	
	@GetMapping("/create")
	public void create() {		
	}
	
	@PostMapping("/create")
	public String create(ReviewCreateDto dto, RedirectAttributes attrs) {
		// save or release ???
		
		Review entity = reviewService.create(dto);
		attrs.addFlashAttribute("createdId", entity.getId()); // 리다이렉트 페이지로 해당 데이터 넘김 
		
		return "redirect:/review/detail";
	}
	
	@GetMapping("/detail")
	public void detail() {
		
	}
	
	@GetMapping("/modify")
	public void modify() {
		
	}
	
}
