package com.example.tutoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tutoring.entity.LikeNotice;


public interface LikeNoticeRepository extends JpaRepository<LikeNotice, Integer> {

}
