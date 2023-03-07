package com.project.film.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.film.domain.Review;
import com.project.film.domain.Users;
import com.project.film.dto.UserJoinDto;
import com.project.film.service.ReviewService;
import com.project.film.service.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {
	
	private final UsersService userService;
	private final ReviewService reviewService;
	
	@GetMapping("/login")
	public String login() {
		return "/user/login";
	}
	
	@GetMapping("/join")
	public String join() {
		return "/user/join";
	}
	
	@PostMapping("/join")
	public String join(UserJoinDto dto, RedirectAttributes attrs) {
		Users user = userService.create(dto.fromEntity());
		attrs.addFlashAttribute("creatdId", user.getIdName());
		return "redirect:/login";
	}
	
	@GetMapping("/mypage")
	public String mypage(String idName, Model model) {
		List<Review> reviewAll = reviewService.readUser(idName);
		
		List<Review> reviewRelease = new ArrayList<>();
		List<Review> reviewSave = new ArrayList<>();
		for (Review r : reviewAll) {
			if (r.getStorage() == 0) {
				reviewSave.add(r);
			} else {
				reviewRelease.add(r);
			}
		}
		
		Integer releaseSize = reviewRelease.size();
		Integer saveSize = reviewSave.size();
		
		model.addAttribute("releaseSize", releaseSize);
		model.addAttribute("saveSize", saveSize);
		model.addAttribute("idName", idName);
		model.addAttribute("reviewSave", reviewSave);
		model.addAttribute("reviewRelease", reviewRelease);
		return "/user/mypage";
	}
	
	@GetMapping("/find") 
	public String find() {
		return "/user/find";
	}
	
	@GetMapping("/checkid")
	@ResponseBody
	public ResponseEntity<String> checkIdName(String idName) {
		String result = userService.checkIdName(idName);
		return ResponseEntity.ok(result);
	}
	
}
