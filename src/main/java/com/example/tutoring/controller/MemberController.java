package com.example.tutoring.controller;

import com.example.tutoring.service.MemberService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	private MemberService memberService;

	@RequestMapping("/loginPage")
	public void InitLoginPage()
	{
		log.info("로그인 페이지 진입");
	}	

	// 회원가입 기능
	@PostMapping("/signup")
	public ResponseEntity<Map<String, Object>> signUp (@RequestBody Map<String, Object> memberData) {

		log.info("----/member/signup API 진입-----");
		log.info("회원가입 요청 데이터: {}", memberData);

		// 회원가입 처리 서비스 호출
		return memberService.signUp(memberData);
	}
	//로그인
	@PostMapping("/login")
	public ResponseEntity<Map<String,Object>> login(@RequestBody Map<String, Object> loginData){
		
		log.info("로그인 데이터 : "+loginData.toString());
		
		return memberService.login(loginData);
		
	}
	
	//토큰 검사
	@PostMapping("/tokenCheck")
	public ResponseEntity<Map<String, Object>> tokenCheck(HttpServletRequest request){
		log.info("----/member/tokenCheck API 진입----");
		String accessToken = request.getHeader("Bearer");
		log.info("엑세스 토큰 : "+accessToken);
		
		return memberService.tokenCheck(accessToken);
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
	
	//로그아웃
	@PostMapping("/logout")
	public ResponseEntity<Map<String,Object>> logout(HttpServletRequest request) {
		log.info("----/member/logout API 진입----");
		String accessToken = request.getHeader("Bearer");
		log.info("엑세스 토큰 : "+accessToken);
		
	    return memberService.logout(accessToken);
	}

}
