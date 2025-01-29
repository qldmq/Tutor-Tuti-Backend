package com.example.tutoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.tutoring.entity.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{

//    @Query("SELECT COUNT(m) > 0 FROM Member m WHERE m.memberId = :memberId")
//    boolean existsByMemberId(@Param("memberId") String memberId);

}
