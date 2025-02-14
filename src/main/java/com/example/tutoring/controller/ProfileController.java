package com.example.tutoring.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.tutoring.service.ProfileService;

@Slf4j
@Controller
@RequestMapping("/profile")
public class ProfileController {

	
	@Autowired
	ProfileService profileService;
	
	@PatchMapping("/image")
	public ResponseEntity<Map<String,Object>> profileImgUpdate(@RequestParam("profileImg") MultipartFile file, HttpServletRequest request)
	{
		log.info("----/profile/image API 진입----");
		String accessToken = request.getHeader("Authorization").substring(7);
		log.info("엑세스 토큰 : "+accessToken);
				
		return profileService.profileImgUpdate(file, accessToken);
	}
}
