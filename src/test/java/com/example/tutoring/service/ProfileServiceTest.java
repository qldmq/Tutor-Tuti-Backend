package com.example.tutoring.service;

import com.example.tutoring.entity.Member;
import com.example.tutoring.entity.Notice;
import com.example.tutoring.jwt.JwtTokenProvider;
import com.example.tutoring.repository.MemberRepository;
import com.example.tutoring.repository.NoticeRepository;
import com.example.tutoring.dto.NoticeDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class ProfileServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private JwtTokenProvider jwtProvider;

    public void setUp() {
        // DB에 테스트용 데이터 삽입 (간단히 할 경우)
        noticeRepository.deleteAll();  // 기존 데이터 삭제
        Notice notice1 = Notice.builder()
                .memberNum(1)
                .content("공지사항 1")
                .createTime(new java.util.Date())
                .likeCnt(5)
                .disLikeCnt(1)
                .build();
        noticeRepository.save(notice1);

        Notice notice2 = Notice.builder()
                .memberNum(1)
                .content("공지사항 2")
                .createTime(new java.util.Date())
                .likeCnt(10)
                .disLikeCnt(3)
                .build();
        noticeRepository.save(notice2);
    }

    // 닉네임 변경 테스트
    @Test
    public void testChangeNickname() {

        // 기존 회원의 memberNum을 사용
        int newMemberNum = 5;  // 존재하는 회원의 memberNum 입력
        String accessToken = jwtProvider.createAccessToken(String.valueOf(newMemberNum));

        log.info("액세스토큰입니다아아 {}", accessToken);

        String newNickname = "lastchangenickname";

        ResponseEntity<Map<String, Object>> response = profileService.changeNickname(newNickname, accessToken);

        log.info("Response Status: {}", response.getStatusCode());
        log.info("Response Body: {}", response.getBody());
    }

}