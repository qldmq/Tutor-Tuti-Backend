package com.example.tutoring.controller;

import com.example.tutoring.service.MemberService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


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
	
	//토큰 만료 확인
	@PostMapping("/accessCheck")
	public ResponseEntity<Map<String, Object>> accessCheck(@RequestBody Map<String,String> accessToken){
		log.info("----/member/accessCheck API 진입----");
		log.info("엑세스 토큰 : "+accessToken);
		
		return memberService.accessCheck(accessToken.get("access"));
	}
	
	//토큰 재발급
	@PostMapping("/reissue")
	public ResponseEntity<Map<String, Object>> reissue(@RequestBody Map<String,String> accessToken){
		log.info("----/member/reissue API 진입----");
		log.info("엑세스 토큰 : "+accessToken);
		
		return memberService.reissue(accessToken.get("access"));
	}
	
	
	
}
