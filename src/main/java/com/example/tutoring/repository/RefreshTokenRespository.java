package com.example.tutoring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tutoring.entity.RefreshToken;

public interface RefreshTokenRespository extends JpaRepository<RefreshToken, Integer> {

	 Optional<RefreshToken> findByReToken(String reToken);
	 void deleteByMemberNum(Integer membernum);
}
