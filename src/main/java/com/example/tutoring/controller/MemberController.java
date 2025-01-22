package com.example.tutoring.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

	
	private final Logger log = LoggerFactory.getLogger(MemberController.class);
	
	@RequestMapping("/loginpage")
	public void InitLoginPage()
	{
		log.info("로그인 페이지 진입");
	}	
			
}
