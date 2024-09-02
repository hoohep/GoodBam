// ImportRestController.java
package com.example.demo.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Import;
import com.example.demo.model.Result;
import com.example.demo.model.Users;
import com.example.demo.repository.ImportRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ResultService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping
public class ImportRestController {

	@Autowired
	private ImportRepository importRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ResultService resultservice;

	@PostMapping("/api/member/import")
	public ResponseEntity<String> importData(@RequestBody Import importRequest) {
		// importRequest ==> POSTMAN으로 전달받은 데이터 KEY VALUE
		// DB에서 사용자 조회
		// userEmail ==> 전달받은 데이터 중 Email 값
		System.out.println(importRequest);
		String userEmail = importRequest.getUser().getUsername();
		// userOptional ==> UserRepository 클래스의 findByEmail를 통해 이메일로 사용자 정보를 조회
		Optional<Users> userOptional = userRepository.findByEmail(userEmail);

		// 사용자가 존재하지 않는 경우
		if (userOptional.isEmpty()) {
			return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
		}
		// Optional 객체에서 실제 Users 객체를 추출
		Users user = userOptional.get();
		importRequest.setUser(user);
		// regdate가 null인 경우 현재 날짜로 설정
		if (importRequest.getRegdate() == null) {
			importRequest.setRegdate(LocalDate.now());
		}
		 // 현재 날짜와 사용자 정보를 기준으로 데이터가 이미 존재하는지 확인
        boolean dataExists = importRepository.existsByUserAndRegdate(user, importRequest.getRegdate());

        if (dataExists) {
            // 이미 해당 날짜에 데이터가 존재하는 경우
            return ResponseEntity.status(409).body("해당 날짜에 이미 저장된 데이터가 존재합니다.");
        }
		
		// Import 객체를 데이터베이스에 저장
		importRepository.save(importRequest);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		Import requestBody = importRequest;
		HttpEntity<Import> requestEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<Result> flastRequest = restTemplate.exchange("http://127.0.0.1:5000/resultrequest", HttpMethod.POST,
				requestEntity, Result.class);

		Result flaskResponse = flastRequest.getBody();
		flaskResponse.setEmail(userEmail);
		flaskResponse.setRdate(LocalDate.now());
		resultservice.saveresult(flaskResponse);
		

		
		
		

		return ResponseEntity.ok("데이터 저장 완료");

	}
}