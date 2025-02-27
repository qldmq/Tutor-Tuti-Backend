package com.example.tutoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tutoring.entity.LikeNotice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeNoticeRepository extends JpaRepository<LikeNotice, Integer> {

    // like 테이블에서 데이터가 있는지 조회
    @Query("SELECT COUNT(*) > 0 FROM LikeNotice l WHERE l.noticeNum = :noticeNum")
    boolean existsByNoticeNum(@Param("noticeNum") int noticeNum);


}