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
public class JwtUtil { //토큰 생성, 유효한 토큰, 사용자 정보확인
	
	private long accessTokenExpTime;
	private Key key; //security 사용시 사용할 (암호화된) Key
	
	//ExpTime, 암호화 전 key로 사용할 문자열
	public JwtUtil(@Value("${jwt.secret}") String secretKey,
			@Value("${jwt.expiration_time}")long accessTokenExpTime) {
		 
		this.accessTokenExpTime = accessTokenExpTime;
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes); //HMAC 알고리즘 적용한 Key 객체 생성
	}
	
	public String createAccessToken(UserDetails userDetails) { //만들어진 토큰을 반환
        return createToken(userDetails, accessTokenExpTime);
    }
	
	private String createToken(UserDetails user, long expireTime) {
    	
    	//Claims : 정보는 담는 조각, 토큰 생성 시 사용할 정보를 담기 위함
        Claims claims = Jwts.claims();
        claims.put("memberId", ((Users) user).getId());
        claims.put("id", user.getUsername());
        claims.put("role", ((Users) user).getRole());

        ZonedDateTime now = ZonedDateTime.now();  //현재 시간 기준 실제 만료 날짜 구하기 위함
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
	
	//받은 토큰에서 Claims 파싱 (토큰을 받았을 때 유효한지)
	public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
	
	public String getUserId(String token) { //username(id)을 가지고 오기위한 메서드
        return parseClaims(token).get("id", String.class);
    }
	
	//유효 토큰 확인
	public boolean validateToken(String token, HttpServletRequest request) {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return true;
   }
}








