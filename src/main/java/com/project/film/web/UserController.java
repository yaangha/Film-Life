package com.project.film.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @RequiredArgsConstructor
@Controller
public class UserController {
	
	@GetMapping("/login")
	public String login() {
		return "/user/login";
	}
	
	@GetMapping("/join")
	public String join() {
		return "/user/join";
	}
	
	@GetMapping("/mypage")
	public String mypage() {
		return "/user/mypage";
	}
	
	@GetMapping("/find") 
	public String find() {
		return "/user/find";
	}
	
}
