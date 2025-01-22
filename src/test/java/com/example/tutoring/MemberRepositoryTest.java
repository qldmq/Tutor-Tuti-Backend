package com.example.tutoring;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import com.example.tutoring.dto.MemberDto;
import com.example.tutoring.entity.Member;
import com.example.tutoring.repository.MemberRepository;

@DataJpaTest
public class MemberRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;
	
	@Test
	@Transactional
	@Commit
	public void testInsertMember()
	{
		
		MemberDto memberDto = MemberDto.builder()
							.email("test@example.com")
							.password("1q2w3e4r5t")
							.login_type(0)
							.phone("01022223333")	
							.nickname("테스트계정")
							.profile_img("/data/img.jpg")
							.build();			
		
		Member member = Member.toEntity(memberDto);
		
		Member savedMember = memberRepository.save(member);
		
		
		assertThat(savedMember);
		assertThat(savedMember.getMember_num()).isNotNull();
        assertThat(savedMember.getEmail()).isEqualTo("test@example.com");
	}
	
	
}
