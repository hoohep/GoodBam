package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>{
	Optional<Users> findById(String id);
	
	//id 값 중복확인
	boolean existsById(String id);
}
