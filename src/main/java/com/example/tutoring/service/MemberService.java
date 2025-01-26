package com.example.tutoring.service;

import com.example.tutoring.dto.MemberDto;
import com.example.tutoring.entity.Member;
import com.example.tutoring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    // 회원가입 처리
    public void signUp(MemberDto memberDto) {
        
        // 1. 중복 검사

        // 2. Member 객체 생성
        Member member = Member.builder()
                .memberId(memberDto.getMemberId())
                .email(memberDto.getEmail())
                .password(memberDto.getPassword())
                // .nickname(memberDto.getNickname())
                .build();

        // 3. DB 저장
        memberRepository.save(member);
    }



}
