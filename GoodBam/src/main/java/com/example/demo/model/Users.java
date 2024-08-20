package com.example.demo.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
// TABLE 만들기
@Entity
@Table(name="USERS")
public class Users implements UserDetails{ //Security에서 사용자 정보를 표현할 때 구현해줘야함!
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) // 자동적으로 1번부터 들어가게 함
	@Column
	private Long u_id;
	
	@Column(name="u_uid", nullable=false, unique=true, length=30)
	private String id;
	
	@Column(name="u_upw",nullable=false)
	private String password;
	
	@Column(name="u_uname",nullable=false)
	private String name;
	
	@Column(name="u_uclassify",nullable=false)
	@Enumerated(EnumType.STRING) 
	private Classify classify; // KAKAO, GENERAL 
	
	@Column(name="u_role",nullable=false)
	@Enumerated(EnumType.STRING) //지정된 값 그대로 문자열로 테이블에 추가
	private Role role; //ADMIN, USER

	// 로그인 시 자동으로 내부적으로 호출되는 메서드들
	// 권한 목록 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	// 사용자 패스워드 리턴
	@Override
	public String getPassword() {
		return password;
	}

	// 사용자 username(id) 리턴
	@Override
	public String getUsername() { // 로그인 시 입력되는 값 
		return id;
	}

	public String getId() {
		return id;
	}

	public Role getRole() {
		return role;
	}
	
}








