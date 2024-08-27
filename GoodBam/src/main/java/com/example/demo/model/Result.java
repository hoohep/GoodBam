package com.example.demo.model;

import java.sql.Timestamp;
import java.time.LocalDate;

//import java.security.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id")
    private Long id;

	@ManyToOne // RESULT 엔티티가 여러 인스턴스의 Users 엔티티를 참조할 수 있음을 나타내는 어노테이션
	@JoinColumn(name = "r_u_uid", referencedColumnName = "u_uid",
				insertable = false, updatable = false)
	private Users user; 
	
    @Column(name = "r_u_uid", nullable = false)
    private String email;  // 필드 이름은 CamelCase로 유지, DB 컬럼은 snake_case

    @Column(name = "r_imp_nm", nullable = false)
    private String impname;

    @Column(name = "r_imp_chg", nullable = false)
    private String impvalue;

    @Column(name = "r_chat", nullable = false)
    private String rchat;

    @Column(name = "r_credate", nullable = false)
    private LocalDate rdate;
	
}
