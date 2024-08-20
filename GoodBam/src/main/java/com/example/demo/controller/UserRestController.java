package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.model.LoginDTO;
import com.example.demo.model.Users;
import com.example.demo.service.UserDetailService;
import com.example.demo.service.UserService;

@RestController
public class UserRestController {
	// React랑 연결할 때 쓰일 RestController
	
	@Autowired
	private UserService service; 
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailService detailService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/api/member/join")
	public ResponseEntity<String> join(Users users) {
		
		service.join(users);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	@PostMapping("/api/member/login")
	// 로그인 메서드 - 로그인할 때 넘겨오는 값을 DTO로 설정
	public ResponseEntity<String> login(LoginDTO dto) {
		authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getId(), dto.getPassword())
            );

        final UserDetails userDetails = detailService.loadUserByUsername(dto.getId());
        String token = jwtUtil.createAccessToken(userDetails);

        return ResponseEntity.status(HttpStatus.OK).body(token);
	}
	
	@GetMapping("/test")
	public String test(){

        return "ok";
	}
}








