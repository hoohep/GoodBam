package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.model.ResultDTO;
import com.example.demo.repository.ResultRepository;

public class ResultService {
 
	@Autowired
	private ResultRepository repository;
	
	@Autowired
	private ResultDTO resultdto;
	
	// 결과화면
	public ResultDTO result(String r_u_uid, String r_credate ) {
		
		if(!repository.existByR_u_uidANDR_credate(r_u_uid, r_credate)){
			
			return null;
		}
		return resultdto;
		
	}
	
}
