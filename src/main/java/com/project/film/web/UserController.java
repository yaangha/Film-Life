package com.project.film.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.film.domain.Users;
import com.project.film.dto.UserJoinDto;
import com.project.film.service.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {
	
	private final UsersService userService;
	
	@GetMapping("/login")
	public String login() {
		return "/user/login";
	}
	
	@PostMapping("/login") // TODO: ajax로 고쳐보기
	public String login(String idName, String password) {
		Boolean result = userService.checkLogin(idName, password);
		if (result) {
			return "redirect:/review/main";
		} else {
			return "redirect:/login";
		}
		
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
	public String mypage() {
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
