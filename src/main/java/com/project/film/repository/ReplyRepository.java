package com.project.film.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.film.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

	List<Reply> findByReviewIdOrderByIdDesc(Integer reviewId);

	List<Reply> findByUsersId(Integer id);

}
