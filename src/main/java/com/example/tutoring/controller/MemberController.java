package com.example.tutoring.controller;

import com.example.tutoring.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
	public ResponseEntity<Map<String, Object>> signUp (@RequestBody Map<String, Object> memberData) {

		log.info("----/member/signup API 진입-----");
		log.info("회원가입 요청 데이터: {}", memberData);

		// 회원가입 처리 서비스 호출
		return memberService.signUp(memberData);
	}

	   // 이메일 인증번호 요청 기능
   	@PostMapping("/sendEmail")
   	@ResponseBody
	public ResponseEntity<Map<String, Object>> sendEmail(@RequestBody Map<String, Object> emailData) {

		String email = emailData.get("email").toString();

		log.info("이메일 요청 api 진입");

		return memberService.sendEmail(email);

	}

	// 이메일 인증번호 확인
	@GetMapping("/checkEmail")
	public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam int checkNum) {

		log.info("인증번호 확인 api 진입");

		return memberService.checkEmail(checkNum);
	}
}
