package com.project.film.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.film.domain.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {
	
	@EntityGraph(attributePaths = "roles")
	Optional<Users> findByIdName(String idName);

}
