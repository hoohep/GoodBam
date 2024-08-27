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

	// 수면 분석 결과 요청
	@GetMapping("/result")
	public ResponseEntity<Result> result(HttpServletRequest request) {
		// react에서 Header에 실어서 요청한 값 가져오기   
		String authorizationHeader = request.getHeader("Authorization");

		String email = null;
		LocalDate currentDate = null;

		// 넘어 온 값에서 토큰 처리하기
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			// 토큰 값 앞에 "Bearer" 지우기
			String token = authorizationHeader.substring(7);
			// 넘어온 token에서 email값만 파싱하기
			email = jwtUtil.getUserId(token);
			// 서버 내에서 현재날짜 가져오기
			currentDate = LocalDateTime.now().toLocalDate();
		}
		
		// DB에서 데이터 가져오기 위한 로직 호출
		Result result = service.result(email, currentDate);
		// 값이 있을경우 body에 실어서 react로 값 보내기
		if (result != null) {
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
	}

	// 수면 결과리스트 목록 요청
	@GetMapping("/resultList")
	public ResponseEntity<List<Result>> resultList(HttpServletRequest request) {
		// react에서 Header에 실어서 요청한 값 가져오기
		String authorizationHeader = request.getHeader("Authorization");

		String email = null;
		
		// 넘어 온 값에서 토큰 처리하기
		if (authorizationHeader != null & authorizationHeader.startsWith("Bearer")) {
			// 토큰 값 앞에 "Bearer" 지우기
			String token = authorizationHeader.substring(7);
			// 넘어온 token에서 email값만 파싱하기
			email = jwtUtil.getUserId(token);
		}
		
		// DB에서 데이터 가져오기 (List형태)
		List<Result> resultList = service.resultList(email);
		System.out.println(resultList.get(1).getRdate());
		if(resultList != null) {
			// 값이 있을경우 body에 실어서 react
			return ResponseEntity.status(HttpStatus.OK).body(resultList); 
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		
	}

}
