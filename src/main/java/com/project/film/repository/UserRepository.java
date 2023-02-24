package com.project.film.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.film.domain.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {


}
