package com.example.tutoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.tutoring.entity.Notice;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    // memberNum에 해당하는 공지글 목록 조회
    List<Notice> findByMemberNum(Integer memberNum);
    
    // 게시글 카운트
	@Query("SELECT COUNT(n) FROM Notice n WHERE n.memberNum = :memberNum")
  	int noticeCount(@Param("memberNum") int memberNum);

    @Query("SELECT n.likeCnt FROM Notice n WHERE n.noticeNum = :noticeNum")
    Integer findLikeCntByNoticeNum(int noticeNum);

    @Query("SELECT n.disLikeCnt FROM Notice n WHERE n.noticeNum = :noticeNum")
    Integer finddisLikeCntByNoticeNum(int noticeNum);

    @Modifying
    @Query("UPDATE Notice n SET n.likeCnt = :likeCnt WHERE n.noticeNum = :noticeNum")
    void updateLikeCount(@Param("noticeNum") int noticeNum, @Param("likeCnt") int likeCnt);

    @Query(value = "SELECT n.noticeNum, n.memberNum, n.content, n.createTime, n.likeCnt, n.disLikeCnt " +
            "FROM notice n WHERE n.memberNum = :memberNum " +
            "ORDER BY n.createTime DESC " +
            "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Object[]> findByMemberNumWithPagination(@Param("memberNum") int memberNum, @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Modifying
    @Query("UPDATE Notice n SET n.disLikeCnt = :disLikeCnt WHERE n.noticeNum = :noticeNum")
    void updateDisLikeCount(@Param("noticeNum") int noticeNum, @Param("disLikeCnt") int disLikeCnt);

}
