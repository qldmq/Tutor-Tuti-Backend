package com.example.tutoring.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.tutoring.entity.Alim;

public interface AlimRepository extends JpaRepository<Alim, Integer>{
	
	@Modifying
	@Query("UPDATE Alim a SET a.readTime = :readTime, a.isRead = :isRead WHERE a.alimNum = :alimNum")
	void readAlim(@Param("readTime")Date readTime, @Param("isRead")boolean isRead, @Param("alimNum")Integer alimNum);
	
	@Query("SELECT COUNT(a) > 0 FROM Alim a WHERE a.memberNum = :memberNum AND a.isRead = false")
	boolean existsUnreadAlim(@Param("memberNum") Integer memberNum);
	
	@Query(value="SELECT * FROM alim a WHERE a.memberNum = :memberNum "+
	"ORDER BY isRead ASC, sendTime DESC "+
	"LIMIT :pageSize OFFSET :offset",nativeQuery = true)
	List<Alim> findAlimList(@Param("memberNum") Integer memberNum, @Param("pageSize")int pageSize, @Param("offset")int offset);
}
