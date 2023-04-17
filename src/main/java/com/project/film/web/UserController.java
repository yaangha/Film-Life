package com.project.film.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.film.domain.Image;
import com.project.film.domain.Review;
import com.project.film.domain.Users;
import com.project.film.dto.ReviewReadDto;
import com.project.film.dto.UserJoinDto;
import com.project.film.repository.ImageRepository;
import com.project.film.service.ImageService;
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
	private final ImageService imageService;
	
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
		List<Image> imageList = imageService.readImg(idName);
		log.info("imageList size ={}", imageList.size());
		List<ReviewReadDto> reviewRelease = new ArrayList<>();
		List<ReviewReadDto> reviewSave = new ArrayList<>();
		
		for (Review r : reviewAll) {
			if (r.getStorage() == 0) {
				ReviewReadDto dto = ReviewReadDto.fromEntity(r, null, null, null, null);
				reviewSave.add(dto);
			} else {
				for (Image i : imageList) {
					if (i != null) {
						if (r.getId() == i.getReview().getId()) {
							ReviewReadDto dto = ReviewReadDto.fromEntity(r, null, null, null, i.getId());
							reviewRelease.add(dto);
							break;
						}
					} else {
						ReviewReadDto dto = ReviewReadDto.fromEntity(r, null, null, null, null);
						reviewRelease.add(dto);
					}
				}
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
	
	@GetMapping("/deleteUser")
	public String deleteUser(String idName) {
		userService.deleteUser(idName);
		return "redirect:/logout";
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
