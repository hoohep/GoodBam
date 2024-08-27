package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>{
	// 사용자 정보를 조회
	Optional<Users> findByEmail(String email); 
	// 이메일로 사용자가 존재하는지 여부를 확인
	boolean existsByEmail(String email); 
}
