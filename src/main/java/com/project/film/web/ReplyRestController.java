package com.project.film.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.film.dto.ReplyReadDto;
import com.project.film.dto.ReplyRegisterDto;
import com.project.film.service.ReplyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reply")
public class ReplyRestController {

	private final ReplyService replyService;
	
	@PostMapping
	public ResponseEntity<Integer> registerReply(@RequestBody ReplyRegisterDto dto) {
		Integer sizeList = replyService.create(dto);
		return ResponseEntity.ok(sizeList);
	}
	
	@GetMapping("/all/{reviewId}")
	public ResponseEntity<List<ReplyReadDto>> readAllReplies(@PathVariable Integer reviewId) {
		List<ReplyReadDto> list = replyService.readReplies(reviewId);
		return ResponseEntity.ok(list);
	}
	
}
