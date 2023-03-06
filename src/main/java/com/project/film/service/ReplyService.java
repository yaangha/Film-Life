package com.project.film.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.film.domain.Reply;
import com.project.film.domain.Review;
import com.project.film.domain.Users;
import com.project.film.dto.ReplyReadDto;
import com.project.film.dto.ReplyRegisterDto;
import com.project.film.repository.ReplyRepository;
import com.project.film.repository.ReviewRepository;
import com.project.film.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReplyService {
	
	private final ReviewRepository reviewRepository;
	private final ReplyRepository replyRepository;
	private final UsersRepository usersRepository;
	
	/**
	 * Reply 데이터를 DB에 등록하는 메서드
	 * @param dto 등록시 필요한 데이터
	 * @return reply PK
	 */
	public Integer create(ReplyRegisterDto dto) {
	
		Review review = reviewRepository.findById(dto.getReviewId()).get();
		Users user = usersRepository.findByIdName(dto.getWriter()).get();
		Reply reply = Reply.builder()
				.review(review).content(dto.getReplyText()).users(user).build();
		reply = replyRepository.save(reply);
		
		return reply.getId();
	}
	
	/**
	 * 해당 리뷰에 등록된 댓글을 불러올 때 사용하는 메서드
	 * @param reviewId 
	 * @return
	 */
	public List<ReplyReadDto> readReplies(Integer reviewId) {
		List<Reply> list = replyRepository.findByReviewIdOrderByIdDesc(reviewId);
		return list.stream()
				.map(ReplyReadDto::fromEntity)
				.toList();
	}
	
	/**
	 * 해당 리뷰에 달린 댓글수 구하는 메서드
	 * @param reviewId
	 * @return
	 */
	public Integer countReply(Integer reviewId) {
		List<Reply> replies = replyRepository.findByReviewIdOrderByIdDesc(reviewId);
		return replies.size();
	}

}
