package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Role;
import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
	//회원가입
	public void join(Users users) {
		
		users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
		//users.setRole(Role.ROLE_USER);
		
		// id중복 아닐시 저장
		if(!repository.existsById(users.getUsername())) {
			repository.save(users);
		}
		
	}
}






