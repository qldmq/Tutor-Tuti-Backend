package com.example.tutoring.service;

import com.example.tutoring.dto.MemberDto;
import com.example.tutoring.entity.Member;
import com.example.tutoring.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 처리
    public Map<String, Object> signUp(Map<String, Object> memberData) {

        String newNick = createNick();
        Map<String, Object> responseMap = new HashMap<>();

        try {
            // 중복 아이디가 있을 경우
            if (checkMemberId(memberData.get("memberId").toString())) {
                responseMap.put("status", 400);
                responseMap.put("message", "이미 존재하는 아이디입니다.");
                return responseMap;
            }

            MemberDto memberDto = MemberDto.builder()
                    .loginType(0)
                    .memberId(memberData.get("memberId").toString())
                    .email(memberData.get("email").toString())
                    .password(passwordEncoder.encode(memberData.get("password").toString()))
                    .nickname(newNick)
                    .introduction("안녕하세요")  // DB 설정대로 출력되도록 수정 필요
                    .build();

            Member member = Member.toEntity(memberDto);
            memberRepository.save(member);
            responseMap.put("status", 200);
        } catch (Exception e) {
            responseMap.put("status", 400);
        }

        return responseMap;
    }

    // 아이디 중복 확인
    public boolean checkMemberId(String memberId) {

        return memberRepository.existsByMemberId(memberId);
    }

    // 닉네임 생성
    public String createNick() {

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        Random random = new Random();
        int randNick =  1000 + random.nextInt(9000);

        return now.format(formatter) + randNick;
    }
}
