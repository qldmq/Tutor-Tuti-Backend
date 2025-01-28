package com.example.tutoring.service;

import com.example.tutoring.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
//@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 회원가입() {

        // given
        Map<String, Object> memberData = new HashMap<>();
        memberData.put("memberId", "testUser2");
        memberData.put("email", "test@example.com");
        memberData.put("password", "password123");
        memberData.put("nickname", "nickname123");

        // when
        Map<String, Object> response = memberService.signUp(memberData);

        // then
        System.out.println(response.get("status"));  // 상태만 출력
        System.out.println(response.get("message"));  // 상태만 출력
    }

}
