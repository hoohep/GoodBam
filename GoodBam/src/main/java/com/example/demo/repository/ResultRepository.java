package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long>{
	
	// 수면분석결과 데이터 찾
	Result findByEmailAndRdate(String email, LocalDate rdate);
    boolean existsByEmailAndRdate(String email, LocalDate rdate);
	
    // 수면분석결과 리스트 
	List<Result> findByEmail(String email);
	boolean existsByEmail(String email);
	
	
    
    
}