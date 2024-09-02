package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.demo.jwt.CustomAuthenticationEntryPoint;
import com.example.demo.jwt.JwtAuthenticationFilter;
import com.example.demo.jwt.JwtExceptionFilter;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.service.UserDetailService;

import jakarta.servlet.DispatcherType;

@Configuration // 설정파일
@EnableWebSecurity // Spring Security 활성화
					// 내부적으로 SpringSecurityFilterChain 동작
public class SecurityConfig {

	@Autowired
	private UserDetailService userDetailService;

	@Autowired
	private JwtUtil jwtUtil;

	// css, js, 이미지 (정적리소스) => 정적리소스가 제대로 적용되지 않는 상황 발생
	@Bean // 수동으로 객체 생성
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/static/**");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // GET요청 제외 요청으로부터 보호, csrf 토큰 포함(위조요청), rest api 사용시에는 설정x
				// 세션 관리 상태 없음 구성
				.sessionManagement(
						sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.formLogin(form -> form.disable()) // 로그인 폼 비활성화
				.httpBasic(AbstractHttpConfigurer::disable) // HTTP 기반 기본 인증 비활성화
				.addFilterBefore(new JwtAuthenticationFilter(userDetailService, jwtUtil),
						UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/member/*").permitAll() // 인증 없이도 접근 가능한 경로 설정
						.requestMatchers("/admin").hasRole("ADMIN") // ADMIN 권한이 있을 경우에만 접근 가능한 경로 설정
						.anyRequest().authenticated() // 그 외의 요청은 무조건 인증 확인
				)
				// 예외 처리 관련 설정
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

		return http.build();
	}

	@Bean // 비밀번호 암호화하기
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// CORS - Cross-origin resource sharing (교차 출처 리소스 공유)
	// 도메인 밖의 다른 도메인으로부터 요청할 수 있게 허용
	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("https://springreact-bqaya4buech6gdcm.koreacentral-01.azurewebsites.net");// 리액트 서버
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");

		// CORS 설정을 URL 경로에 따라 정의하기 위해 생성
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 CORS 설정
		return new CorsFilter(source);
	}

	// 인증 공급자 : UserDetailsService, PasswordEncoder 활용 인증 논리 구현
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	// 인증 관리자 : 인증 공급자를 활용 인증 처리
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
