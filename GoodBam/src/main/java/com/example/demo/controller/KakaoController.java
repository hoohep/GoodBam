package com.example.demo.controller;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.model.KakaoAcDTO;
import com.example.demo.model.Users;
import com.example.demo.service.UserDetailService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@RestController
public class KakaoController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserDetailService detailService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
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
		// 메서드 중복 출력 확인용*JwtExceptionFilter 수정 후 해결완료)
		// System.out.println("KakaoLogin 메서드 호출됨");
		
		// 클라이언트에서 받은 인가 코드
		String code = kakaoAcDTO.getCode();
		System.out.println("인가 코드 : " + code);
		// 인가 코드를 사용해 액세스 토큰을 요청하기 위한 준비
		// HTTP 통신을 위한 도구로 RESTful API 웹 서비스와의 상호작용을 쉽게 외부 도메인에서 데이터를 가져오거나 전송할 때 사용되는 스프링 프레임워크의 클래스
		RestTemplate restTemplate = new RestTemplate();
		// 카카오에 액세스 토큰을 요청하기 위한 파라미터 설정(헤더, 바디 설정)
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/x-www-form-urlencoded");
		String requestBody = "grant_type=authorization_code" + "&client_id=" + KAKAO_API_KEY + "&redirect_uri="
				+ REDIRECT_URI + "&code=" + code;
		// 요청을 위한 엔티티 생성 (헤더와 바디 포함)
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
		// 액세스 토큰 요청
		ResponseEntity<String> accessTokenRequest = restTemplate.exchange(KAKAO_TOKEN_URL, HttpMethod.POST,
				requestEntity, String.class);

		// 로그 출력으로 토큰 응답 확인
		String accessToken = accessTokenRequest.getBody();
		System.out.println("토큰 JSON : " + accessToken);

		ObjectMapper objectMapper = new ObjectMapper();
		// JSON 파싱하여 JsonNode 객체 생성
		JsonNode tokenNode = objectMapper.readTree(accessToken);
		// access_token 값 추출
		String accessToken2 = tokenNode.path("access_token").asText();
		// 추출된 access_token 출력
		System.out.println("토큰값: " + accessToken2);

		// 액세스 토큰을 사용해 사용자 정보 요청
		RestTemplate restTemplate2 = new RestTemplate();
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

		// 사용자 정보에서 email과 nickname 추출
		JsonNode userInfoNode = objectMapper.readTree(userInfoJson);
		String nickname = userInfoNode.path("properties").path("nickname").asText();
		String email = userInfoNode.path("kakao_account").path("email").asText();

		// email과 nickname 출력
		System.out.println("닉네임: " + nickname);
		System.out.println("이메일: " + email);

		// Users 객체 생성 후 DB에 저장
		Users kakaoUser = new Users();
		kakaoUser.setEmail(email); // u_uid -> email
		kakaoUser.setName(nickname); // u_uname -> nickname

		// 카카오 사용자 회원가입 서비스 로직 호출
		userService.joinKakao(kakaoUser);

		// 사용자의 이메일을 기반으로 UserDetails 객체를 가져와 JWT 토큰 생성
        UserDetails userDetails = detailService.loadUserByUsername(email);
        String jwtToken = jwtUtil.createAccessToken(userDetails);
		
        // 토큰이 잘 생성됐는지 확인하기 위해 JWT 토큰에서 이메일과 역할 추출
        String userEmail = jwtUtil.getUserId(jwtToken);
        String userRole = jwtUtil.getUserRole(jwtToken);

        // 추출한 이메일과 역할 출력
        System.out.println("토큰에 담긴 이메일 : " + userEmail);
        System.out.println("토큰에 담긴 역할 : " + userRole);
        
		// 받은 액세스 토큰을 클라이언트로 반환
		return ResponseEntity.ok(jwtToken);
	}

}
