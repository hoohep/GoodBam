package com.example.demo.jwt;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.demo.model.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil { // 토큰 생성, 유효한 토큰, 사용자 정보확인

	private long accessTokenExpTime; // 만료시간
	private Key key; // security 사용시 사용할 (암호화된) Key

	// ExpTime, 암호화 전 key로 사용할 문자열
	public JwtUtil(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration_time}") long accessTokenExpTime) {

		this.accessTokenExpTime = accessTokenExpTime;
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes); // HMAC 알고리즘 적용한 Key 객체 생성
	}

	// 만들어진 토큰을 반환
	public String createAccessToken(UserDetails userDetails) { 
		return createToken(userDetails, accessTokenExpTime);
	}

	// 토큰 생성
	private String createToken(UserDetails user, long expireTime) {
		// Claims : 정보를 담는 조각, 토큰 생성 시 사용할 정보를 담기 위함
		Claims claims = Jwts.claims();
		// Jwt에 포함될 사용자 식별 정보 claims에 추가
		claims.put("email", user.getUsername());
		claims.put("role", ((Users) user).getRole());

		// 현재 시간 기준 실제 만료 날짜 구하기 위함
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime tokenValidity = now.plusSeconds(expireTime);
		
		// Jwt 생성 및 반환
		return Jwts.builder().setClaims(claims).setIssuedAt(Date.from(now.toInstant()))
				.setExpiration(Date.from(tokenValidity.toInstant())).signWith(key, SignatureAlgorithm.HS256).compact();
	}

	// 받은 토큰에서 Claims 파싱 
	public Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
	// username(email)을 가지고 오기위한 메서드
	public String getUserId(String token) { 
		return parseClaims(token).get("email", String.class);
	}
	// role(admin, user)를 가지고 오기위한 메서드
	public String getUserRole(String token) { 
		return parseClaims(token).get("role", String.class);
	}

	// 유효 토큰 확인 
	public boolean validateToken(String token, HttpServletRequest request) {
		Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		return true;
	}
}
