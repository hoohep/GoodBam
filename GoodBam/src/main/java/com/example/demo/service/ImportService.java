package com.example.demo.service;

import com.example.demo.model.Import;
import com.example.demo.model.Users;
import com.example.demo.repository.ImportRepository;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportService {

    @Autowired
    private ImportRepository importRepository;

    // 데이터를 저장하기 전, 같은 날에 해당 유저의 데이터가 이미 존재하는지 확인 후 저장
    public boolean saveImportData(Import importData) {
        Users user = importData.getUser();
        LocalDate regdate = importData.getRegdate();
        
        // 새로운 existsByUserAndRegdate 메서드를 사용하여 중복 확인
        if (importRepository.existsByUserAndRegdate(user, regdate)) {
            // 이미 당일 데이터가 존재하면 저장하지 않음
            return false;
        } else {
            // 당일 데이터가 없으면 저장
            importRepository.save(importData);
            return true;
        }
    }
}