package com.example.demo.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long>{
	
	Result findByEmailAndRdate(String email, LocalDate rdate);

    boolean existsByEmailAndRdate(String email, LocalDate rdate);
	
	
}