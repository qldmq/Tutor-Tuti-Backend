package com.example.tutoring.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.tutoring.service.OAuthService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/oauth")
public class OAuthController {

	@Autowired
	OAuthService oAuthService;
	
	@PostMapping("/naver")
	public ResponseEntity<Map<String, Object>> naverLogin(@RequestBody Map<String,Object> loginData)
	{
		log.info("네이버 로그인 토큰 수신");		
		return oAuthService.naverLogin(loginData);		
	}

	@GetMapping("/kakao")
	public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestParam String code) {

		log.info("kakao 로그인 api 진입");
		return oAuthService.kakaoLogin(code);
	}

}
