package com.example.demo.controller;

import com.example.demo.model.Import;
import com.example.demo.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member/import")
public class ImportRestController {

    @Autowired
    private ImportService importService;

    // 포스트맨에서 데이터를 받을 엔드포인트
    @PostMapping
    public ResponseEntity<String> createImportData(@RequestBody Import importData) {
    	 // 데이터 저장 부분을 주석 처리
        // boolean isSaved = importService.saveImportData(importData);
        
        // 콘솔에 데이터 출력
        System.out.println("수신된 데이터: " + importData);
        
        // 응답 반환 (저장 성공과 실패 메시지 구분 없이 테스트)
        return new ResponseEntity<>("데이터 수신 완료", HttpStatus.OK);
    }
}