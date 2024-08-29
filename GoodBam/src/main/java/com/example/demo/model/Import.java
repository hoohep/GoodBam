package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "IMPVALUE")
public class Import {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "i_id")
	private Long id;
	
//	@Column(name = "i_u_uid", nullable = false, length = 30)
//	private String email;
	
	@Column(name = "i_regdate", nullable = false) // 날짜
	private LocalDate regdate;
	
	@Column(name = "i_calories", nullable = false) // 칼로리
	private float calories;
	
	@Column(name = "i_step", nullable = false) // 걸음수
	private float step;
	
	@Column(name = "i_sleep_efficiency", nullable = false) // 수면효율
	private float sleepeff;
	
	@Column(name = "i_height", nullable = false) // 산장
	private float height;
	
	@Column(name="i_gender",nullable=false) // 성별
	@Enumerated(EnumType.STRING) 
	private GenderClassify genderclassify; // MAN, WOMAN 
	
	@Column(name = "i_age", nullable = false) // 연령
	private float age;
	
	@Column(name = "i_weight", nullable = false) // 체중
	private float weight;
	
	@Column(name = "i_fat_rate", nullable = false) //체지방률
	private float fatrate;
	
	// USERS 엔티티와의 관계를 설정
    @ManyToOne //Import 엔티티가 여러 인스턴스의 Users 엔티티를 참조할 수 있음을 나타내는 어노테이션
    @JoinColumn(name = "i_u_uid", referencedColumnName = "u_uid", nullable = false)
    private Users user;
}
