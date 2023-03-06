package com.project.film.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.film.domain.ReviewScore;

public interface ReviewScoreRepository extends JpaRepository<ReviewScore, Integer> {
	
	// select r from ReviewScore r where r.review.id = :reviewId and r.users.id = :usersId 
	@Query("select r from REVIEWSCORE r where r.review.id = :reviewId and r.users.id = :usersId")
	ReviewScore findHeart(@Param(value = "reviewId") Integer reviewId, @Param(value = "usersId") Integer usersId);

	List<ReviewScore> findByReviewId(Integer reviewId);

}
