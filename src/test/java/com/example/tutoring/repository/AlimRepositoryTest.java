package com.example.tutoring.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.example.tutoring.dto.AlimDto;
import com.example.tutoring.dto.MemberDto;
import com.example.tutoring.entity.Alim;
import com.example.tutoring.entity.Member;
import com.example.tutoring.type.AlimType;

@DataJpaTest
public class AlimRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private AlimRepository alimRepository;
	
	@Autowired
    private EntityManager entityManager; 
	
	@Test
	public void alimTest()
	{
		MemberDto member1 = MemberDto.builder()
                .memberId("member1")
                .email("test@example.com")
                .password("1q2w3e4r5t")
                .loginType(0)
                .nickname("테스트계정")
                .profileImg("/data/img.jpg")
                .build();	
		
		Member member1Saved = memberRepository.saveAndFlush(Member.toEntity(member1));

		//싫어요 알림 테스트
		AlimDto alimDto = AlimDto.builder()
				.memberNum(member1Saved.getMemberNum())
				.alimMsg("테스트님이 게시글에 싫어요를 눌렀습니다.")
				.alimType(AlimType.TYPE_DISLIKE)
				.isRead(false)
				.sendTime(new Date())
				.readTime(new Date())
				.build();
		
		Alim alim = alimRepository.saveAndFlush(Alim.toEntity(alimDto));
		
		assertThat(alim).isNotNull();
		assertThat(alim.getAlimNum()).isNotNull();
		assertThat(alim.getAlimType()).isEqualTo(AlimType.TYPE_DISLIKE);
				
		//알림 읽음처리
		alimRepository.readAlim(new Date(), true, alim.getAlimNum());
		alimRepository.flush();  // 변경된 내용을 DB에 반영
	    entityManager.clear();	
		Optional<Alim> readAlim = alimRepository.findById(alim.getAlimNum());		
		
		assertThat(readAlim.get().getReadTime().after(readAlim.get().getSendTime())).isTrue();
		assertThat(readAlim.get().isRead()).isEqualTo(true);
		
	}
}
