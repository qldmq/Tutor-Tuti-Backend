package com.example.tutoring.controller;


import com.example.tutoring.dto.MemberDto;
import com.example.tutoring.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	private final Logger log = LoggerFactory.getLogger(MemberController.class);

	@Autowired
	private MemberService memberService;

	@RequestMapping("/loginPage")
	public void InitLoginPage()
	{
		log.info("로그인 페이지 진입");
	}	

	// 회원가입 기능
	@PostMapping("/signup")
	public void signUp (@RequestBody Map<String, Object> paramMap) {
		log.info("signup API 진입");
		log.info("회원가입 요청 데이터: {}", paramMap);

		String memberId = (String) paramMap.get("memberId");
		String email = (String) paramMap.get("email");
		String password = (String) paramMap.get("password");
		String nickname = "nickname";

		// 유효성 검사: 필수 값 체크
		if (memberId == null || email == null || password == null) {
			log.error("회원가입 필수 값이 누락되었습니다.");
			// 적절한 응답을 반환하거나 예외를 던질 수 있습니다.
			return;
		}

		MemberDto memberDto = MemberDto.builder()
				.memberId(memberId)
				.email(email)
				.password(password)
				.nickname(nickname)
				.build();

		// 회원가입 처리 서비스 호출
		memberService.signUp(memberDto);
	}

}
