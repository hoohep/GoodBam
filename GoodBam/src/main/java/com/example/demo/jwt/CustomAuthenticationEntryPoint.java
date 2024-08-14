package com.example.demo.jwt;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.demo.model.Code;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		//Exception Filter 에서 담아 놨던 값 확인
		Integer exception = (Integer)request.getAttribute("exception");

        if(exception == null) {
            setResponse(response, Code.UNKNOWN_ERROR);
        }
        //잘못된 타입의 토큰인 경우
        else if(exception.equals(Code.WRONG_TYPE_TOKEN.getCode())) {
            setResponse(response, Code.WRONG_TYPE_TOKEN);
        }
        //토큰 만료된 경우
        else if(exception.equals(Code.EXPIRED_TOKEN.getCode())) {
            setResponse(response, Code.EXPIRED_TOKEN);
        }
        //지원되지 않는 토큰인 경우
        else if(exception.equals(Code.UNSUPPORTED_TOKEN.getCode())) {
            setResponse(response, Code.UNSUPPORTED_TOKEN);
        }
        else {
            setResponse(response, Code.ACCESS_DENIED);
        }
		
	}
	
    private void setResponse(HttpServletResponse response, Code code) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> data = new HashMap<>();
        data.put("status", HttpServletResponse.SC_UNAUTHORIZED); //401 (권한없음)
        data.put("message", "Unauthorized - " + code.getMessage());

        //ServletOuputStream 활용 응답 바디에 해당 상태코드, 메시지 전송
        response.getOutputStream().println(objectMapper.writeValueAsString(data));
    }
}
