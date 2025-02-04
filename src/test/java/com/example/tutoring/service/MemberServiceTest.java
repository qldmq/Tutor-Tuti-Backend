package com.example.tutoring.service;

import com.example.tutoring.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
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
        memberData.put("memberId", "testUser1234");
        memberData.put("email", "test@example.com");
        memberData.put("password", "password123");
        memberData.put("nickname", "nickname123");

        // when
        ResponseEntity<Map<String, Object>> responseEntity = memberService.signUp(memberData);
        Map<String, Object> response = responseEntity.getBody();


        // then
        log.info("message: {}", response.get("message"));
    }


    // 이메일 전송 테스트
    @Test
    public void 이메일_인증번호_전송() {

        // given
        String testEmail = "ksh13345@naver.com";

        // when
        ResponseEntity<Map<String, Object>> responseEntity = memberService.sendEmail(testEmail);
        Map<String, Object> response = responseEntity.getBody();

        log.info("status: {}", response.get("status"));
        log.info("message: {}", response.get("message"));
        log.info("checkNum: {}", response.get("checkNum"));
    }

    // 인증번호 검증 테스트 (성공)
    @Test
    void testCheckEmail_Success() {

        // given
        int checkNum = 123456;
        memberService.checkNum = checkNum;

        // when
        ResponseEntity<Map<String, Object>> response = memberService.checkEmail(checkNum);

        // then
        assertEquals(200, response.getStatusCodeValue());
    }

    // 인증번호 검증 테스트 (실패)
    @Test
    void testCheckEmail_Failure() {

        // given
        int checkNum = 123456;
        memberService.checkNum = 654321;

        // when
        ResponseEntity<Map<String, Object>> response = memberService.checkEmail(checkNum);

        // then
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void 아이디_찾기_테스트() {

        // given
        String testEmail = "ksh1334514@naver.com";

        // when
        ResponseEntity<Map<String, Object>> responseEntity = memberService.findId(testEmail);
        Map<String, Object> response = responseEntity.getBody();

        // then
        String memberId = (String) response.get("memberId");
        log.info("찾은 아이디: {}", memberId); // 아이디 출력
    }
}
