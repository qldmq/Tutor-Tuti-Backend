package com.example.tutoring.service;

import com.example.tutoring.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

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
        System.out.println("회원가입 테스트 실행");

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
        System.out.println("message" + response.get("message"));  // 상태만 출력
    }


    // 이메일 전송 테스트
    @Test
    public void 이메일_인증번호_전송() {
        System.out.println("이메일 인증번호 전송 테스트 실행");

        // given
        String testEmail = "ksh13345@naver.com";

        // when
        ResponseEntity<Map<String, Object>> responseEntity = memberService.sendEmail(testEmail);
        Map<String, Object> response = responseEntity.getBody();



        System.out.println("status: " + response.get("status"));
        System.out.println("message: " + response.get("message"));
        System.out.println("checkNum: " + response.get("checkNum"));  // 인증번호 출력
    }

    // 인증번호 검증 테스트 (성공)
    @Test
    void testCheckEmail_Success() {
        System.out.println("인증번호 검증 테스트(성공)");

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
        System.out.println("인증번호 검증 테스트(실패)");

        // given
        int checkNum = 123456;
        memberService.checkNum = 654321;

        // when
        ResponseEntity<Map<String, Object>> response = memberService.checkEmail(checkNum);

        // then
        assertEquals(400, response.getStatusCodeValue());
    }
}
