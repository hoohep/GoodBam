package com.example.demo.repository;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long>{
	Optional<Result> findByRUUidAndRCredate(String r_u_uid, Timestamp r_credate);
	
	boolean existsByRUUidAndRCredate(String r_u_uid, Timestamp r_credate);
	
	
}