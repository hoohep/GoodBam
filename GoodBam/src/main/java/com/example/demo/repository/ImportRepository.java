package com.example.demo.repository;

import com.example.demo.model.Import;
import com.example.demo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ImportRepository extends JpaRepository<Import, Long> {

    // 특정 유저의 특정 날짜에 해당하는 Import 데이터를 조회하는 메서드
    Optional<Import> findByUserAndRegdate(Users user, LocalDate regdate);

    // 특정 유저의 특정 날짜에 해당하는 Import 데이터가 존재하는지 확인하는 메서드
    boolean existsByUserAndRegdate(Users user, LocalDate regdate);
}