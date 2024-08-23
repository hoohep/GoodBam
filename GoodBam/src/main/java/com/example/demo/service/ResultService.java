package com.example.demo.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Result;
import com.example.demo.repository.ResultRepository;

@Service
public class ResultService {
 
	@Autowired
	private ResultRepository repository;
	

	
	// 결과화면
	public Result result(String id, LocalDate date ) {
		
		if(!repository.existsByEmailAndRdate(id, date)){
			
			return null;
		}
		Result result = repository.findByEmailAndRdate(id, date);
		return result;
		
	}
	
}
