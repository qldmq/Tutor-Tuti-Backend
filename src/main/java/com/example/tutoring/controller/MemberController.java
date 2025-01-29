package com.example.tutoring.controller;

import com.example.tutoring.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	@ResponseBody
	public Map<String, Object> signUp (@RequestBody Map<String, Object> memberData) {
		log.info("----/member/signup API 진입-----");
		log.info("회원가입 요청 데이터: {}", memberData);

		// 회원가입 처리 서비스 호출
		return memberService.signUp(memberData);
	}

	
}
