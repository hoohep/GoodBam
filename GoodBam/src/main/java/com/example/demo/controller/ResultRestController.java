package com.example.demo.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

	// 수면 분석 결과
	@GetMapping("/result")
	public ResponseEntity<Result> result(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");

		String email = null;
		LocalDate currentDate = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			// token에서 email값만 파싱
			email = jwtUtil.getUserId(token);
			// 현재날짜 가져오기
			currentDate = LocalDateTime.now().toLocalDate();
		}
		
		// DB에서 데이터 가져오기
		Result result = service.result(email, currentDate);
		System.out.println(result.getRchat());
		if (result != null) {
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
	}

	// 수면 결과리스트 목록
	@GetMapping("/resultList")
	public ResponseEntity<List<Result>> resultList(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");

		String email = null;
		
		if (authorizationHeader != null & authorizationHeader.startsWith("Bearer")) {
			String token = authorizationHeader.substring(7);
			email = jwtUtil.getUserId(token);
		}
		
		// DB에서 데이터 가져오기 (List형태)
		List<Result> resultList = service.resultList(email);
		System.out.println(resultList.get(1).getRdate());
		if(resultList != null) {
			return ResponseEntity.status(HttpStatus.OK).body(resultList); 
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		
	}

}
