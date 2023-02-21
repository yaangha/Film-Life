package com.project.film.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @Requried..
@Controller
@RequestMapping("/review")
public class ReviewController {
	
	@GetMapping("/main")
	public void main() {
	
	}
	
	@GetMapping("/create")
	public void create() {
		
	}
	
	@GetMapping("/detail")
	public void detail() {
		
	}
	
	@GetMapping("/modify")
	public void modify() {
		
	}
	
}
