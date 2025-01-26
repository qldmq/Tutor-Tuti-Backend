package com.example.tutoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.tutoring.entity.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{



}
