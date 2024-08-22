package com.example.demo.controller;



import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.model.ResultDTO;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ResultRestController {

	@Autowired
	private JwtUtil jwtUtil;
	
	// 오늘 날짜 생성
	LocalDateTime localDateTime = LocalDateTime.now();
	
		
	@PostMapping("/result")
	public ResponseEntity<ResultDTO> result(HttpServletRequest request){
		String authorizationHeader = request.getHeader("Authorization");
		
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			String email = jwtUtil.getUserId(token);
			Timestamp currentDate = Timestamp.valueOf(localDateTime);
		}
		
		return null;
		
	}
	
}
