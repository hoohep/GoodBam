package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long>{
	Optional<Result> findByR_u_uidANDR_credate(String r_u_uid, String r_credate);
	
	boolean existByR_u_uidANDR_credate(String r_u_uid, String r_credate);
	
	
}