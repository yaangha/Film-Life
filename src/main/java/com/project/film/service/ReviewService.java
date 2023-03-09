package com.project.film.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.film.domain.Reply;
import com.project.film.domain.Review;
import com.project.film.domain.ReviewScore;
import com.project.film.domain.Users;
import com.project.film.dto.ReviewCreateDto;
import com.project.film.dto.ReviewReadDto;
import com.project.film.repository.ReplyRepository;
import com.project.film.repository.ReviewRepository;
import com.project.film.repository.ReviewScoreRepository;
import com.project.film.repository.UsersRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
	
	private final ReviewRepository reviewRepository;
	private final ReviewScoreRepository reviewScoreRepository;
	private final UsersRepository usersRepository;
	private final ReplyRepository replyRepository;
	
	/**
	 * create 창에서 데이터 저장
	 * @param dto 테이블에 저장할 데이터
	 * @return 객체 리턴
	 */
	public Review create(ReviewCreateDto dto) {
		
		// dto.setStorage(0);
		Review entity = reviewRepository.save(dto.toEntity());
		
		return entity;
	}
	
	/**
	 * detail에서 보여줄 데이터 찾는 메소드
	 * @param reviewId review table PK
	 * @return 
	 */
	public Review read(Integer reviewId) {
		return reviewRepository.findById(reviewId).get();
	}

	public List<Review> readAll() {
		return reviewRepository.findByOrderByIdDesc();
	}

	public List<ReviewReadDto> readReleaseAll() {
		List<Review> listAll = readAll();
		List<ReviewReadDto> list = new ArrayList<>();
		for (Review r : listAll) {
			if (r.getStorage() == 1) {
				List<ReviewScore> reviewScore = reviewScoreRepository.findByReviewId(r.getId());
				Integer heart = 0;
				for (ReviewScore rs : reviewScore) {
					heart += rs.getHeart();
				}
				
				List<Reply> reply = replyRepository.findByReviewIdOrderByIdDesc(r.getId());
				Integer countReply = reply.size();
				
				list.add(ReviewReadDto.fromEntity(r, heart, 0, countReply));
			}
		}
		return list;
	}
	
	public ReviewReadDto readReview(Integer reviewId) {
		ReviewReadDto reviewDto = null;
		
		Review review = reviewRepository.findById(reviewId).get();
		
		List<ReviewScore> reviewScoreList = reviewScoreRepository.findByReviewId(reviewId);
		Integer heart = 0;
		for (ReviewScore rs : reviewScoreList) {
			heart += rs.getHeart();
		}
		
		List<Reply> reply = replyRepository.findByReviewIdOrderByIdDesc(reviewId);
		
		reviewDto = ReviewReadDto.fromEntity(review, heart, 0, reply.size());
		
		return reviewDto;
	}

	public Integer addHeart(Integer reviewId, String idName) {
		Users user = usersRepository.findByIdName(idName).get();
		Review review = reviewRepository.findById(reviewId).get();
		ReviewScore rs = reviewScoreRepository.findHeart(reviewId, user.getId());

		if (rs == null) { // 사용자 데이터가 없을 경우에는 create
			rs = ReviewScore.builder().users(user).review(review).heart(1).build();
			reviewScoreRepository.save(rs);
		} else { // 사용자 데이터가 있을 경우에는 update
			rs.setHeart(1);
			reviewScoreRepository.save(rs);
		}
		
		Integer result = 0;
		List<ReviewScore> list = reviewScoreRepository.findByReviewId(reviewId);
		for (ReviewScore score : list) {
			result += score.getHeart();
		}
		
		return result;
	}

	public Integer deleteHeart(Integer reviewId, String idName) {
		Users user = usersRepository.findByIdName(idName).get();
		ReviewScore rs = reviewScoreRepository.findHeart(reviewId, user.getId());
		rs.setHeart(0);
		reviewScoreRepository.save(rs);	
		
		Integer result = 0;
		List<ReviewScore> list = reviewScoreRepository.findByReviewId(reviewId);
		for (ReviewScore score : list) {
			result += score.getHeart();
		}
		
		return result;
	}

	public List<ReviewReadDto> search(String type, String keyword) {
		List<ReviewReadDto> list = new ArrayList<>();
		
		switch(type) {
		case "r":
			List<Review> reviews = reviewRepository.findByAuthorIgnoreCaseContainingOrTitleIgnoreCaseContainingOrContentIgnoreCaseContainingOrderByIdDesc(keyword, keyword, keyword);
			for (Review r : reviews) {
				if (r.getStorage() == 1) {
					Integer heart = 0;
					List<ReviewScore> reviewScore = reviewScoreRepository.findByReviewId(r.getId());
					for (ReviewScore rs : reviewScore) {
						heart += rs.getHeart();
					}
					
					List<Reply> reply = replyRepository.findByReviewIdOrderByIdDesc(r.getId());
					
					ReviewReadDto dto = ReviewReadDto.fromEntity(r, heart, 0, reply.size());
					list.add(dto);
				}
			}
			break;
		}
		
		return list;
	}
	
	/**
	 * mypage에서 사용자가 작성한 리뷰 불러올 때 사용
	 * @param idName
	 * @return
	 */
	public List<Review> readUser(String idName) {
		log.info("idName??={}", idName);
		return reviewRepository.findByAuthorOrderByIdDesc(idName);
	}

	public void delete(Integer reviewId) {
		List<ReviewScore> rs = reviewScoreRepository.findByReviewId(reviewId);
		for (ReviewScore r : rs) {
			reviewScoreRepository.deleteById(r.getId());
		}
		
		List<Reply> replies = replyRepository.findByReviewIdOrderByIdDesc(reviewId);
		for (Reply rp : replies) {
			replyRepository.deleteById(rp.getId());
		}
		reviewRepository.deleteById(reviewId);
	}
	
	@Transactional // save() 하지않아도 저장됨
	public Integer modify(ReviewCreateDto dto) {
		Review review = reviewRepository.findById(dto.getReviewId()).get();
		log.info("here?111={}" ,dto.getTitle());
		review.update(dto.getTitle(), dto.getContent(), dto.getScore());
		log.info("here?222={},{}", review.getTitle(), dto.getTitle());
		return dto.getReviewId();
	}
	
	/**
	 * 조회수 증가 메서드
	 * @param idName 
	 * @param reviewId
	 * @param request
	 * @param response
	 */
	public void updateWatchCount(String idName, Integer reviewId, HttpServletRequest request, HttpServletResponse response) {
		Users user = (idName.equals("Anonymous")) ? (Users.builder().idName("Anonymous").username("Anonymous").build()) : (usersRepository.findByIdName(idName).get());
//		if (idName.equals("Anonymous")) {
//			user = Users.builder().idName("Anonymous").username("Anonymous").build();
//		} else {
//			user = usersRepository.findByIdName(idName).get();
//		}
		Review review = reviewRepository.findById(reviewId).get();
		ReviewScore reviewScore = reviewScoreRepository.findByReviewIdAndUsersId(reviewId, user.getId());
		
		Cookie oldCookie = null;
		Cookie[] cookies = request.getCookies(); // 현재 존재하는 쿠키 리스트
		
		if (cookies != null) { // 쿠키가 존재할 때 reviewCookie 유무체크
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("reviewCookie")) {
					oldCookie = cookie;
				}
			}
		}
		
		if (oldCookie != null) { // reviewCookie 존재시
			if (!oldCookie.getValue().contains("[" + reviewId.toString() + "-" + user.getId().toString() + "]")) {
				if (reviewScore == null) {
					reviewScore = ReviewScore.builder().users(user).review(review).heart(0).watch(0).build();
					reviewScore = reviewScore.updateWatchCount();
					reviewScoreRepository.save(reviewScore);
				} else {
					reviewScore = reviewScore.updateWatchCount();
					reviewScoreRepository.save(reviewScore);
				}
				
				oldCookie.setValue(oldCookie.getValue() + "[" + reviewId + "-" + user.getId() + "]");
				oldCookie.setPath("/");
				oldCookie.setMaxAge(60 * 30);
				response.addCookie(oldCookie);
			}
		} else { // reviewCookie 없을시
			reviewScore = ReviewScore.builder().users(user).review(review).heart(0).watch(0).build();
			reviewScore = reviewScore.updateWatchCount();
			reviewScoreRepository.save(reviewScore);
			
			Cookie newCookie = new Cookie("reviewCookie", "[" + reviewId + "-" + user.getId() + "]");
			newCookie.setPath("/");
			newCookie.setMaxAge(60 * 30);
			response.addCookie(newCookie);
		}
	}

	

}
