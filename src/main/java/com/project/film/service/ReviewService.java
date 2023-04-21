package com.project.film.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.film.domain.Image;
import com.project.film.domain.Reply;
import com.project.film.domain.Review;
import com.project.film.domain.ReviewScore;
import com.project.film.domain.Users;
import com.project.film.dto.ReviewCreateDto;
import com.project.film.dto.ReviewReadDto;
import com.project.film.repository.ImageRepository;
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
	private final ImageRepository imageRepository;
	
	/**
	 * create 창에서 데이터 저장
	 * @param dto 테이블에 저장할 데이터
	 * @return 객체 리턴
	 */
	public Review create(ReviewCreateDto dto) {
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
	
	// 발행된 리뷰들만 List에 저장하는 메서드 
	public List<ReviewReadDto> readReleaseAll() {
		List<Review> listAll = readAll();
		List<ReviewReadDto> list = new ArrayList<>();
		for (Review r : listAll) {
			if (r.getStorage() == 1) {
				ReviewReadDto dto = addData(r);
				list.add(dto);
			}
		}
		return list;
	}
	
	/**
	 * reviewId로 찾은 리뷰에서 화면에 보여줄 데이터만 저장하는 메서드 
	 * @param reviewId
	 * @return
	 */
	public ReviewReadDto readReview(Integer reviewId) {
		ReviewReadDto reviewDto = null;
		
		Review review = reviewRepository.findById(reviewId).get();
		
		Integer[] score = countScore(reviewId);
		
		List<Reply> reply = replyRepository.findByReviewIdOrderByIdDesc(reviewId);
		List<Image> image = imageRepository.findByReviewId(review.getId());
		if (image.size() != 0) {
			reviewDto = ReviewReadDto.fromEntity(review, score[0], score[1], reply.size(), image.get(0).getId());
		} else {
			reviewDto = ReviewReadDto.fromEntity(review, score[0], score[1], reply.size(), null);
		}
		
		return reviewDto;
	}
	
	/**
	 * 메인화면에서 검색시 사용 
	 * @param type 어떤 option인지  
	 * @param keyword 검색하고자 하는 키워드 
	 * @return
	 */
	public List<ReviewReadDto> search(String type, String keyword) {
		List<ReviewReadDto> list = new ArrayList<>();
		
		switch(type) {
		case "r":
			List<Review> reviews = reviewRepository.findByAuthorIgnoreCaseContainingOrTitleIgnoreCaseContainingOrContentIgnoreCaseContainingOrderByIdDesc(keyword, keyword, keyword);
			for (Review r : reviews) {
				if (r.getStorage() == 1) {
					ReviewReadDto dto = addData(r);
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
		
		List<Image> images = imageRepository.findByReviewId(reviewId);
		for (Image i : images) {
			imageRepository.delete(i);
		}
		
		reviewRepository.deleteById(reviewId);
	}
	
	@Transactional // save() 하지않아도 저장됨
	public Integer modify(ReviewCreateDto dto) {
		Review review = reviewRepository.findById(dto.getReviewId()).get();
		review.setStorage(1);
		review.update(dto.getTitle(), dto.getContent(), dto.getScore(), review.getStorage());
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
		log.info("reviewService updateWatchCount 1");
		Users user = null;
		if (idName.equals("Anonymous")) {
			if (usersRepository.findByIdName(idName).get() != null) {
				user = usersRepository.findByIdName(idName).get();
			} else {
				user = Users.builder().idName("Anonymous").username("Anonymous").build();
				usersRepository.save(user);
			}
		} else {
			user = usersRepository.findByIdName(idName).get();
		}
		log.info("user check!!");
		Review review = reviewRepository.findById(reviewId).get();
		ReviewScore reviewScore = reviewScoreRepository.findScore(reviewId, user.getId());
		if (reviewScore == null) {
			reviewScore = ReviewScore.builder().users(user).review(review).heart(0).watch(0).build();
			reviewScoreRepository.save(reviewScore);
		}
		log.info("Cookie!! reviewScore = {}", reviewScore.getId());
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
				reviewScore = reviewScore.updateWatchCount();
				reviewScoreRepository.save(reviewScore);
				log.info("Cookie!! reviewScore2 = {}", reviewScore.getId());
				
				oldCookie.setValue(oldCookie.getValue() + "[" + reviewId + "-" + user.getId() + "]");
				oldCookie.setPath("/");
				oldCookie.setMaxAge(60 * 30);
				response.addCookie(oldCookie);
			}
		} else { // reviewCookie 없을시
			reviewScore = reviewScore.updateWatchCount();
			reviewScoreRepository.save(reviewScore);
			log.info("Cookie!! reviewScore3 = {}", reviewScore.getId());
			
			Cookie newCookie = new Cookie("reviewCookie", "[" + reviewId + "-" + user.getId() + "]");
			newCookie.setPath("/");
			newCookie.setMaxAge(60 * 30);
			response.addCookie(newCookie);
		}
	}
	
	// 좋아요 추가시 사용 
	public Integer addHeart(Integer reviewId, String idName) {
		Users user = usersRepository.findByIdName(idName).get();
		Review review = reviewRepository.findById(reviewId).get();
		ReviewScore rs = reviewScoreRepository.findScore(reviewId, user.getId());

		if (rs == null) { // 사용자 데이터가 없을 경우에는 create
			rs = ReviewScore.builder().users(user).review(review).heart(1).build();
			reviewScoreRepository.save(rs);
		} else { // 사용자 데이터가 있을 경우에는 update
			rs.setHeart(1);
			reviewScoreRepository.save(rs);
		}
		
		Integer result = totalHeart(reviewId);
		
		return result;
	}

	// 좋아요 삭제시 사용 
	public Integer deleteHeart(Integer reviewId, String idName) {
		Users user = usersRepository.findByIdName(idName).get();
		ReviewScore rs = reviewScoreRepository.findScore(reviewId, user.getId());
		rs.setHeart(0);
		reviewScoreRepository.save(rs);	
		
		Integer result = totalHeart(reviewId);
		
		return result;
	}
	
	public Integer[] countScore(Integer reviewId) {
		Integer[] score = new Integer[2];
		
		Integer heart = 0;
		Integer watch = 0;
		List<ReviewScore> reviewScore = reviewScoreRepository.findByReviewId(reviewId);
		for (ReviewScore rs : reviewScore) {
			heart += rs.getHeart();
			watch += rs.getWatch();
			}
		
		score[0] = heart;
		score[1] = watch;
		
		return score;
	}
	
	/**
	 * 디테일 페이지에서 다른 리뷰들 보여줄 때 사용  
	 * @param reviewId 제외할 리뷰 아이디  
	 * @return 보여줄 리뷰들 리스트 
	 */
	public List<ReviewReadDto> readOtherReviews(Integer reviewId) {
		List<Review> otherReviews = reviewRepository.findOtherReviews(reviewId);
		List<ReviewReadDto> list = new ArrayList<>();
		if (otherReviews.size() > 6) {
			for (int i = 0; i < 6; i++) {
				saveOtherReviews(list, otherReviews.get(i));
			}
		} else {
			for (Review r : otherReviews) {
				saveOtherReviews(list, r);
			}
		}
		
		return list;
	}
	
	private void saveOtherReviews(List<ReviewReadDto> list, Review review) {
		if (review.getStorage() == 1) {
			ReviewReadDto dto = addData(review);
			list.add(dto);
		}
	}

	// 중복되는 코드는 메서드 만들어서 사용
	public Integer totalHeart(Integer reviewId) {
		Integer result = 0;
		List<ReviewScore> list = reviewScoreRepository.findByReviewId(reviewId);
		for (ReviewScore rs : list) {
			result += rs.getHeart();
		}
		
		return result;
	}
	
	private ReviewReadDto addData(Review review) {
		Integer[] score = countScore(review.getId());
		
		List<Reply> reply = replyRepository.findByReviewIdOrderByIdDesc(review.getId());
		Integer countReply = reply.size();
		
		List<Image> image = imageRepository.findByReviewId(review.getId());
		Long imageId = (image.size() == 0)? null : image.get(0).getId();
		
		ReviewReadDto dto = ReviewReadDto.fromEntity(review, score[0], score[1], countReply, imageId);
		
		return dto;
	}

}
