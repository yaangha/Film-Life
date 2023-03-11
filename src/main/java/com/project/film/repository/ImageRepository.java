package com.project.film.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.film.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

	List<Image> findByReviewId(Integer reviewId);

	Image findByFilePath(String imagePath);

	void deleteByReviewId(Integer reviewId);
}
