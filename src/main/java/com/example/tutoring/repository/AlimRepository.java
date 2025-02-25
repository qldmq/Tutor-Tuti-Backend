package com.example.tutoring.repository;

import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.tutoring.entity.Alim;

public interface AlimRepository extends JpaRepository<Alim, Integer>{
	
	@Modifying
	@Query("UPDATE Alim a SET a.readTime = :readTime, a.isRead = :isRead WHERE a.alimNum = :alimNum")
	void readAlim(@Param("readTime")Date readTime, @Param("isRead")boolean isRead, @Param("alimNum")Integer alimNum);
	
}
