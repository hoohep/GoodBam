package com.example.demo.jwt;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.model.Code;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter{

	//예외상황이 발생했을 경우 각 예외상황에 대한 코드를 request에 저장 => EntryPoint(오류처리) 
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			filterChain.doFilter(request, response);
		}catch (ExpiredJwtException e){
            //만료 에러
            request.setAttribute("exception", Code.EXPIRED_TOKEN.getCode());
        } catch (MalformedJwtException e){
            //변조 에러
            request.setAttribute("exception", Code.WRONG_TYPE_TOKEN.getCode());
        } catch (SignatureException e){
            //형식, 길이 에러
            request.setAttribute("exception", Code.WRONG_TYPE_TOKEN.getCode());
        }
		
		filterChain.doFilter(request, response);
	}

}



