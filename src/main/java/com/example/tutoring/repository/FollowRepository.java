package com.example.tutoring.repository;


import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.tutoring.entity.Follow;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {

	//팔로우 체크
	@Query("SELECT COUNT(f) FROM Follow f WHERE f.followerMemberNum = :followerMemberNum AND f.followingMemberNum = :followingMemberNum")
	int followCheck(@Param("followerMemberNum") int followerNum, @Param("followingMemberNum") int followingNum);
	 
	//맞팔로우 체크
	@Query("SELECT COUNT(f) FROM Follow f WHERE (f.followerMemberNum = :followerMemberNum AND f.followingMemberNum = :followingMemberNum) "+
	"OR (f.followingMemberNum = :followerMemberNum AND f.followerMemberNum = :followingMemberNum)")
	int eachFollowCheck(@Param("followerMemberNum") int followerNum, @Param("followingMemberNum") int followingNum);
	 	
    //내가 팔로우한 멤버 목록 조회
    @Query(value="SELECT m.memberNum, m.nickname, m.profileImg, m.introduction " +
    	       "FROM follow f JOIN member m ON f.followingMemberNum = m.memberNum " +
    	       "WHERE f.followerMemberNum = :followerMemberNum " +
               "ORDER BY m.nickname ASC " +
               "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
	List<Object[]> findFollowingMemberList(@Param("followerMemberNum") int followerMemberNum, @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Query(value = "SELECT m.memberNum, m.nickname, m.profileImg, m.introduction " +
            "FROM follow f JOIN member m ON f.followerMemberNum = m.memberNum " +
            "WHERE f.followingMemberNum = :followingMemberNum " +
            "ORDER BY m.nickname ASC " +
            "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Object[]> findFollowerMemberList(@Param("followingMemberNum") int followingMemberNum, @Param("pageSize") int pageSize, @Param("offset") int offset);


    //팔로우 취소(내가 팔로우 하는)
    @Modifying
    @Transactional
    @Query("DELETE FROM Follow f WHERE f.followerMemberNum = :followerMemberNum AND f.followingMemberNum = :followingMemberNum")
    void unFollowMember(@Param("followerMemberNum") int followerNum, @Param("followingMemberNum") int followingNum);

    //내가 팔로우한 멤버 목록 조회(검색)
    @Query(value="SELECT m.memberNum, m.nickname, m.profileImg, m.introduction " +
    	       "FROM follow f JOIN member m ON f.followingMemberNum = m.memberNum " +
    	       "WHERE f.followerMemberNum = :followerMemberNum AND m.nickname LIKE :searchName "+
    	       "ORDER BY m.nickname ASC " +
    	       "LIMIT :pageSize OFFSET :offset", nativeQuery = true)    	       
    	List<Object[]> findSearchFollowingMemberList(@Param("followerMemberNum") int followerMemberNum, @Param("searchName")String searchName, @Param("pageSize") int pageSize, @Param("offset") int offset);

    //나를 팔로우하는 멤버 목록 조회(검색)
    @Query(value = "SELECT m.memberNum, m.nickname, m.profileImg, m.introduction " +
    	       "FROM follow f JOIN member m ON f.followerMemberNum = m.memberNum " +
    	       "WHERE f.followingMemberNum = :followingMemberNum AND m.nickname LIKE :searchName "+
    	       "ORDER BY m.nickname ASC " +
    	       "LIMIT :pageSize OFFSET :offset", nativeQuery = true)  
    	List<Object[]> findSearchFollowerMemberList(@Param("followingMemberNum") int followingMemberNum, @Param("searchName")String searchName,  @Param("pageSize") int pageSize, @Param("offset") int offset);

    //팔로워 삭제(나를 팔로우 하는)
    @Modifying
    @Transactional
    @Query("DELETE FROM Follow f WHERE f.followerMemberNum = :followerMemberNum AND f.followingMemberNum = :followingMemberNum")
    void deleteFollowMember(@Param("followerMemberNum") int followerNum, @Param("followingMemberNum") int followingNum);
    
    
    //팔로워 카운트(회원을 팔로우하는)
  	@Query("SELECT COUNT(f) FROM Follow f WHERE f.followingMemberNum = :memberNum")
  	int followerCount(@Param("memberNum") int memberNum);

  	//팔로잉 카운트(회원이 팔로우하는)
  	@Query("SELECT COUNT(f) FROM Follow f WHERE f.followerMemberNum = :memberNum")
  	int followingCount(@Param("memberNum") int memberNum);
  	  	
  	@Query(value = "SELECT DISTINCT m.memberNum, m.nickname, m.profileImg " +
            "FROM member m " +
            "JOIN follow f ON m.memberNum = f.followingMemberNum " +
            "JOIN notice n ON m.memberNum = n.memberNum " +
            "WHERE f.followerMemberNum = :memberNum " +
            "AND DATE_ADD(n.createTime, INTERVAL 1 DAY) > NOW() " +
            "LIMIT :pageSize OFFSET :offset", 
    nativeQuery = true)
  	List<Object[]> findLastNoticeFollowMember(@Param("memberNum") int memberNum, @Param("pageSize") int pageSize, @Param("offset") int offset);
  	
  	
  	
}
