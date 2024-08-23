package com.example.demo.controller;

import com.example.demo.model.KakaoAcDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@RestController
public class KakaoController {

	// Kakao 토큰을 요청할 URL
	private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
	// Kakao API의 클라이언트 키
	private final String KAKAO_API_KEY = "0196bd96b83188ce5806bb730eff40d5";
	// Kakao에서 인가 코드를 받을 때 사용했던 리다이렉트 URI
	private final String REDIRECT_URI = "http://localhost:3000/kakao";
	// Kakao 사용자 정보를 요청할 URL
	private final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

	// 로그인 요청(클라이언트로부터 인가코드 받고 서버에선 클라이언트로 토큰 보내고 토큰 사용해서 프로필 불러와서 DB에 저장 )
	@PostMapping(value = "/api/member/kakaologin")
	public ResponseEntity<String> KakaoLogin(KakaoAcDTO kakaoAcDTO) throws Exception {
		// 클라이언트에서 받은 인가 코드
		String code = kakaoAcDTO.getCode();
		System.out.println("인가 코드 : "+code);
		// 인가 코드를 사용해 액세스 토큰을 요청하기 위한 준비
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/x-www-form-urlencoded");

		// 카카오에 액세스 토큰을 요청하기 위한 파라미터 설정
		String requestBody = "grant_type=authorization_code" + "&client_id=" + KAKAO_API_KEY + "&redirect_uri="
				+ REDIRECT_URI + "&code=" + code;

		// 요청을 위한 엔티티 생성 (헤더와 바디 포함)
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
		// 액세스 토큰 요청
		ResponseEntity<String> accessTokenResponse = restTemplate.exchange(KAKAO_TOKEN_URL, HttpMethod.POST,
				requestEntity, String.class);

		// 로그 출력으로 응답 확인
		String accessToken = accessTokenResponse.getBody();
		System.out.println("토큰 : " + accessToken);
/////
		ObjectMapper objectMapper = new ObjectMapper();
        // JSON 파싱하여 JsonNode 객체 생성
        JsonNode rootNode = objectMapper.readTree(accessToken);        
        // access_token 값 추출
        String accessToken2 = rootNode.path("access_token").asText();        
        // 추출된 access_token 출력
        System.out.println("Access Token: " + accessToken2);
		
		
		
		RestTemplate restTemplate2 = new RestTemplate();
		// 액세스 토큰을 사용해 사용자 정보 요청
		HttpHeaders userInfoHeaders = new HttpHeaders();
		userInfoHeaders.set("Authorization", "Bearer " + accessToken2);
		// 요청을 위한 엔티티 생성	
		HttpEntity<String> userInfoEntity = new HttpEntity<>(userInfoHeaders);
		// 사용자 정보 요청
		ResponseEntity<String> userInfoResponse = restTemplate2.exchange(KAKAO_USER_INFO_URL, HttpMethod.GET,
				userInfoEntity, String.class);
		
		// 사용자 정보 JSON 문자열
		String userInfoJson = userInfoResponse.getBody();
		System.out.println("사용자 정보 JSON: " + userInfoJson);

//		// Jackson ObjectMapper를 사용해 JSON 문자열을 DTO로 변환
//		ObjectMapper objectMapper = new ObjectMapper();
//		KakaoUserInfoDTO userInfo = objectMapper.readValue(userInfoJson, KakaoUserInfoDTO.class);
//
//		// 이메일과 닉네임 추출
//		String email = userInfo.getKakao_account().getEmail();
//		String nickname = userInfo.getProperties().getNickname();
//
//		System.out.println("이메일 확인 : " + email);
//		System.out.println("닉네임 확인 : " + nickname);
///		
		// 받은 액세스 토큰을 클라이언트로 반환
		return ResponseEntity.ok(accessToken);
	}

}
