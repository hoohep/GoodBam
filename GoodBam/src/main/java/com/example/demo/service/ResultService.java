package com.example.demo.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.model.ResultDTO;
import com.example.demo.repository.ResultRepository;

public class ResultService {
 
	@Autowired
	private ResultRepository repository;
	
	@Autowired
	private ResultDTO resultdto;
	
	// 결과화면
	public void result(String r_u_uid, Timestamp r_credate ) {
		
		if(!repository.existsByRUUidAndRCredate(r_u_uid, r_credate)){
			
//			return null;
		}
//		return resultdto;
		
	}
	
}
