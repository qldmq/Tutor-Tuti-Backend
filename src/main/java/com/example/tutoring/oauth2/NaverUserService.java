package com.example.tutoring.oauth2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.tutoring.dto.MemberDto;
import com.example.tutoring.entity.Member;
import com.example.tutoring.jwt.JwtTokenProvider;
import com.example.tutoring.repository.MemberRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class NaverUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
	
		
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {		
		OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
		Map<String,Object> attributes = oAuth2User.getAttributes();
		Map<String,Object> response = (Map<String,Object>) attributes.get("response");
		
		String email = (String)response.get("email");
		
		Optional<Member> naverMember = memberRepository.findByNaverMember(email);
		
		Member member; 
		
		//기존회원
		if(naverMember.isPresent())
		{
			member = naverMember.get();
		}
		//신규회원
		else 
		{
			 MemberDto memberDto = MemberDto.builder()
	                    .loginType(0)
	                    .memberId(email)
	                    .email(email)
	                    .password("naverPw")
	                    .nickname(createNick())
	                    .introduction("안녕하세요") 
	                    .build();
			member = Member.toEntity(memberDto);					
			
			memberRepository.saveAndFlush(member);
		}
		
		String accessToken = jwtTokenProvider.createAccessToken(Integer.toString(member.getMemberNum()));		
				
		
		return new CustomerOAuth2User(oAuth2User, accessToken, member);
	}

	
	// 닉네임 생성
	private String createNick() {

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        Random random = new Random();
        int randNick =  1000 + random.nextInt(9000);

        return now.format(formatter) + randNick;
    }  
	
	
}
