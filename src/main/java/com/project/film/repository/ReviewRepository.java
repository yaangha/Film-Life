package com.project.film.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.film.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	List<Review> findByOrderByIdDesc(); // ID 내림차순으로 전체 목록 볼 때 사용

}
