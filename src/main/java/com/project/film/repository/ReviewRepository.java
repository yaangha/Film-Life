package com.project.film.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.film.domain.Review;
import com.project.film.dto.ReviewReadDto;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	List<Review> findByOrderByIdDesc();

	List<Review> findByAuthorIgnoreCaseContainingOrTitleIgnoreCaseContainingOrContentIgnoreCaseContainingOrderByIdDesc(String author, String title, String content); // Reveiw Search

	List<Review> findByAuthorOrderByIdDesc(String author);

	// select r from REVIEW r where r.id != :reviewId order by r.id desc
	@Query("select r from REVIEW r where r.id != :reviewId and r.storage = 1 order by r.id desc")
	List<Review> findOtherReviews(@Param(value = "reviewId") Integer reviewId);

}
