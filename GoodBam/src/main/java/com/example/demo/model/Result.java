package com.example.demo.model;

import java.sql.Timestamp;

//import java.security.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="RESVALUE")
public class Result {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column
	private Long r_id;
	
	@Column(name="r_u_uid", nullable=false, unique=true)
	private String RUUid;
	
	@Column(nullable=false)
	private String r_imp_nm;
	
	@Column(nullable=false)
	private String r_imp_chg;
	
	@Column(nullable=false)
	private String r_chat;
	
	@Column(name="r_credate", nullable=false)
	private Timestamp RCredate;
	
}
