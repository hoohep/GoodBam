package com.example.demo.controller;

import com.example.demo.model.KakaoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@RestController
public class KakaoController {

	// Kakao 토큰을 요청할 URL
	private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
	// Kakao API의 클라이언트 키
	private final String KAKAO_API_KEY = "0196bd96b83188ce5806bb730eff40d5";
	// Kakao에서 인가 코드를 받을 때 사용했던 리다이렉트 URI
	private final String REDIRECT_URI = "http://localhost:3000/kakao";

	// 로그인 요청(인가코드 받고 토큰 보내고)

	@PostMapping(value = "/api/member/kakaologin")
	public ResponseEntity<String> KakaoLogin(KakaoDTO kakaoDTO) {
		// 클라이언트에서 받은 인가 코드
		String code = kakaoDTO.getCode();
		System.out.println(code);
		// 인가 코드를 사용해 액세스 토큰을 요청하기 위한 준비
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/x-www-form-urlencoded");

		// Kakao에 액세스 토큰을 요청하기 위한 파라미터 설정
		String requestBody = "grant_type=authorization_code" + "&client_id=" + KAKAO_API_KEY + "&redirect_uri="
				+ REDIRECT_URI + "&code=" + code;

		// 요청을 위한 엔티티 생성 (헤더와 바디 포함)
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
		// 액세스 토큰 요청
		ResponseEntity<String> accessToken = restTemplate.exchange(KAKAO_TOKEN_URL, HttpMethod.POST, requestEntity,
				String.class);

		// 로그 출력으로 응답 확인
		System.out.println("응답: " + accessToken.getBody());

		// 받은 액세스 토큰을 클라이언트로 반환
		return ResponseEntity.ok(accessToken.getBody());
	}


}
