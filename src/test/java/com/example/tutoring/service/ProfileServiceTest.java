package com.example.tutoring.service;

import com.example.tutoring.entity.Notice;
import com.example.tutoring.jwt.JwtTokenProvider;
import com.example.tutoring.repository.NoticeRepository;
import com.example.tutoring.dto.NoticeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProfileServiceTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private JwtTokenProvider jwtProvider;


    @BeforeEach
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

    @Test
    public void testGetNoticesByMemberNum() {
        // 수동으로 액세스 토큰 생성
        String accessToken = jwtProvider.createAccessToken("1");  // 여기서 '1'은 memberNum

        // ProfileService를 통해 memberNum이 1인 공지사항 목록을 조회
        ResponseEntity<Map<String, Object>> response = profileService.myNotice(accessToken);

        // 공지사항 리스트 확인
        assertEquals(200, response.getStatusCodeValue()); // 성공 여부 확인
        List<NoticeDto> noticeDtos = (List<NoticeDto>) response.getBody().get("notices");
    }

}