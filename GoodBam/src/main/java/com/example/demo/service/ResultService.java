package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Result;
import com.example.demo.repository.ResultRepository;

@Service
public class ResultService {
 
	@Autowired
	private ResultRepository repository;
	
	// 수면분석결과 데이터 넘기기
	public Result result(String id, LocalDate date ) {
		
		if(!repository.existsByEmailAndRdate(id, date)){		
			return null;
		}
		// DB에 값 존재하는 경우 데이터 Result 형태로 넘기기
		Result result = repository.findByEmailAndRdate(id, date);
		return result;
		
	}
	
	// 수면분석결과 데이터 리스트 형태로 넘기기
	public List<Result> resultList(String email){
		if(!repository.existsByEmail(email)){
			return null;
		}
		// DB에 값 존재하는 경우 데이터 List<result> 형태로 넘기기
		List<Result> resultList = repository.findByEmail(email);
		return resultList;		
		
	}

	
}
