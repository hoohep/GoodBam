package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Classify;
import com.example.demo.model.Role;
import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
	// 일반 사용자 회원가입
	public void join(Users users) {
		users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
		users.setRole(Role.ROLE_USER);
		users.setClassify(Classify.ROLE_GENERAL);
		
		// 이메일로 중복 여부를 체크
		if(!repository.existsByEmail(users.getUsername())) {
			repository.save(users);
		}
		
	}
	// 카카오 사용자 회원가입
    public void joinKakao(Users users) {
        users.setPassword(null); // 카카오 로그인에는 패스워드가 필요 없음
        users.setRole(Role.ROLE_USER);
        users.setClassify(Classify.ROLE_KAKAO);
     // 이메일로 중복 여부를 체크
        if (!repository.existsByEmail(users.getUsername())) {
            repository.save(users); // 중복이 아닌 경우 저장
        }
    }
}






