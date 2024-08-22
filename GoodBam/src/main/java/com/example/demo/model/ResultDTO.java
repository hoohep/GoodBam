package com.example.demo.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTO {
	
	private String r_imp_nm;
	private String r_imp_chg;
	private String r_chat;
	private Timestamp r_credate; 
}
