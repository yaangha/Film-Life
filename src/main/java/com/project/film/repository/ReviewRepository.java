package com.project.film.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.film.domain.Review;
import com.project.film.dto.ReviewReadDto;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	List<Review> findByOrderByIdDesc();

	List<Review> findByAuthorIgnoreCaseContainingOrTitleIgnoreCaseContainingOrContentIgnoreCaseContainingOrderByIdDesc(String author, String title, String content); // Reveiw Search

	List<Review> findByAuthorOrderByIdDesc(String author);

}
