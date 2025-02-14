package com.example.tutoring.repository;


import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.tutoring.dto.FollowResponseDto;
import com.example.tutoring.entity.Follow;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {

	//팔로우 체크
	@Query("SELECT COUNT(f) FROM Follow f WHERE f.followerMemberNum = :followerMemberNum AND f.followingMemberNum = :followingMemberNum")
	int followCheck(@Param("followerMemberNum") int followerNum, @Param("followingMemberNum") int followingNum);
	   
    //내가 팔로우한 멤버 목록 조회
    @Query("SELECT new com.example.tutoring.dto.FollowResponseDto(m.nickname, m.profileImg, m.introduction) " +
    	       "FROM Follow f JOIN Member m ON f.followingMemberNum = m.memberNum " +
    	       "WHERE f.followerMemberNum = :followerMemberNum")
    	List<FollowResponseDto> findFollowingMemberList(@Param("followerMemberNum") int followerMemberNum);

    //나를 팔로우하는 멤버 목록 조회
    @Query("SELECT new com.example.tutoring.dto.FollowResponseDto(m.nickname, m.profileImg, m.introduction) " +
    	       "FROM Follow f JOIN Member m ON f.followerMemberNum = m.memberNum " +
    	       "WHERE f.followingMemberNum = :followingMemberNum")
    	List<FollowResponseDto> findFollowerMemberList(@Param("followingMemberNum") int followingMemberNum);

    //팔로우 취소
    @Modifying
    @Transactional
    @Query("DELETE FROM Follow f WHERE f.followerMemberNum = :followerMemberNum AND f.followingMemberNum = :followingMemberNum")
    void unFollowMember(@Param("followerMemberNum") int followerNum, @Param("followingMemberNum") int followingNum);
}
