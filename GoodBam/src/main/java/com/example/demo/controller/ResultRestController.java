package com.example.demo.controller;



import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.model.Result;
import com.example.demo.service.ResultService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ResultRestController {

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private ResultService service;
	
	
	// 오늘 날짜 생성
//	LocalDateTime localDateTime = LocalDateTime.now();
	
		
	@GetMapping("/result")
	public void result(HttpServletRequest request){
		String authorizationHeader = request.getHeader("Authorization");
		
		String email = null;
		LocalDate currentDate = null;
		
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			 email = jwtUtil.getUserId(token);
			 currentDate = LocalDateTime.now().toLocalDate();
		}
				
		Result result = service.result(email,currentDate);
		System.out.println(result.getRdate());
		//return ResponseEntity.status(HttpStatus.OK).body(result);
		
	}
	
}
