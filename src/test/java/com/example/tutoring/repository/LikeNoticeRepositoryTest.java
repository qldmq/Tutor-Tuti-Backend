package com.example.tutoring.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.tutoring.dto.LikeNoticeDto;
import com.example.tutoring.dto.MemberDto;
import com.example.tutoring.dto.NoticeDto;
import com.example.tutoring.entity.LikeNotice;
import com.example.tutoring.entity.Member;
import com.example.tutoring.entity.Notice;

@DataJpaTest
public class LikeNoticeRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private NoticeRepository noticeRepository;
	
	@Autowired
	private LikeNoticeRepository likeNoticeRepository;
	
	
	@Test
	public void likeTest()
	{
		
		MemberDto member1 = MemberDto.builder()
                .memberId("member1")
                .email("test@example.com")
                .password("1q2w3e4r5t")
                .loginType(0)
                .nickname("테스트계정")
                .profileImg("/data/img.jpg")
                .build();	
		
		MemberDto member2 = MemberDto.builder()
                .memberId("member2")
                .email("test2@example.com")
                .password("1q2w3e4r5t")
                .loginType(0)
                .nickname("테스트계정")
                .profileImg("/data/img.jpg")
                .build();
		
		Member member1Saved = memberRepository.saveAndFlush(Member.toEntity(member1));
		Member member2Saved =memberRepository.saveAndFlush(Member.toEntity(member2));
		
		NoticeDto noticeDto = NoticeDto.builder()
                .memberNum(member1Saved.getMemberNum()) 
                .content("공지 내용")
                .createTime(new Date())
                .build();
		
		Notice notice = noticeRepository.saveAndFlush(Notice.toEntity(noticeDto));
		
		LikeNoticeDto likeDto = LikeNoticeDto.builder()
								.memberNum(member2Saved.getMemberNum())
								.noticeNum(notice.getNoticeNum())
								.likedAt(new Date())
								.build();
		LikeNotice likeNotice = likeNoticeRepository.saveAndFlush(LikeNotice.toEntity(likeDto));
		
		assertThat(likeNotice).isNotNull();
		assertThat(likeNotice.getLikeNum()).isNotNull();
		
	}
	
	
}
