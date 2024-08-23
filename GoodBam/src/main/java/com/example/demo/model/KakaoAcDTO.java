package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor

// 클라이언트에서 전달된 인가코드를 저장할 DTO
public class KakaoAcDTO {
	 private String code;
}
