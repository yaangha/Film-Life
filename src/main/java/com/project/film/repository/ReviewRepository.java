package com.project.film.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.film.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

}
