package com.example.tutoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tutoring.entity.DisLikeNotice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DisLikeNoticeRepository extends JpaRepository<DisLikeNotice, Integer> {

    // disLike 테이블에서 데이터가 있는지 조회
    @Query("SELECT COUNT(*) > 0 FROM DisLikeNotice l WHERE l.noticeNum = :noticeNum")
    boolean existsByNoticeNum(@Param("noticeNum") int noticeNum);

    @Query("SELECT COUNT(l) > 0 FROM DisLikeNotice l WHERE l.memberNum = :memberNum AND l.noticeNum = :noticeNum")
    boolean existsByMemberNumAndNoticeNum(@Param("memberNum") int memberNum, @Param("noticeNum") int noticeNum);

    @Transactional
    @Modifying
    @Query("DELETE FROM DisLikeNotice l WHERE l.memberNum = :memberNum AND l.noticeNum = :noticeNum")
    void deleteByMemberNumAnAndNoticeNum(@Param("memberNum") int memberNum, @Param("noticeNum") int noticeNum);
}
