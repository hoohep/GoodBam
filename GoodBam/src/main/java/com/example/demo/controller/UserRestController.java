package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.model.LoginDTO;
import com.example.demo.model.Users;
import com.example.demo.service.UserDetailService;
import com.example.demo.service.UserService;
import com.example.demo.service.UserService.EmailAlreadyExistsException;

@RestController
public class UserRestController {

	@Autowired
	private UserService service;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailService detailService;

	@Autowired
	private JwtUtil jwtUtil;

	// 회원가입 요청
	@PostMapping("/api/member/join")
	public ResponseEntity<String> join(Users users) {
		// 회원가입 기능 로직 호출
		String result = service.join(users);
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	
	// 로그인 요청
	@PostMapping("/api/member/login")
	public ResponseEntity<String> login(LoginDTO dto) {
		// 사용자가 제공한 이메일과 비밀번호를 기반으로 인증 시도
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
		// DB에서 정보 조회 로직 호출
		final UserDetails userDetails = detailService.loadUserByUsername(dto.getEmail());
		// 토큰 발급
		String token = jwtUtil.createAccessToken(userDetails);
		// 토큰 반환
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}

}