package com.example.tutoring.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@PostMapping("/followClick")
	public ResponseEntity<Map<String,Object>> followClick(@RequestBody Map<String,Object> followerData, HttpServletRequest request)
	{
		log.info("----/profile/followClick API 진입----");
		String accessToken = request.getHeader("Authorization").substring(7);
		String followerNickName = followerData.get("followerNickName").toString();
		
		log.info("엑세스 토큰 : "+accessToken);				
		return profileService.followClick(followerNickName, accessToken);
	}
	
	//나를 팔로우하는 사람의 목록
	@GetMapping("/myFollower")
	public ResponseEntity<?> myFollower(HttpServletRequest request)
	{
		log.info("----/profile/myFollower API 진입----");
		String accessToken = request.getHeader("Authorization").substring(7);	
		log.info("엑세스 토큰 : "+accessToken);		
		
		return profileService.myFollower(accessToken);
	}
	
	//내가 팔로우하는 사람의 목록
	@GetMapping("/myFollowing")
	public ResponseEntity<?> myFollowing(HttpServletRequest request)
	{
		log.info("----/profile/myFollowing API 진입----");
		String accessToken = request.getHeader("Authorization").substring(7);	
		log.info("엑세스 토큰 : "+accessToken);		
		
		return profileService.myFollowing(accessToken);
	}
	
	@DeleteMapping("/unfollow")
	public ResponseEntity<Map<String,Object>> unFollow(@RequestBody Map<String,Object> followerData, HttpServletRequest request)
	{
		log.info("----/profile/unfollow API 진입----");
		String accessToken = request.getHeader("Authorization").substring(7);
		String followerNickName = followerData.get("followerNickName").toString();
		
		return profileService.unFollow(followerNickName, accessToken);
	}
	
	//나를 팔로우 하는 사람의 목록
	@GetMapping("/searchFollower")
	public ResponseEntity<?> searchFollower(@RequestParam(value = "searchName", required = false)String searchName , HttpServletRequest request)
	{
		log.info("----/profile/searchFollower API 진입----");
		if(searchName == null)
			searchName ="";
		String accessToken = request.getHeader("Authorization").substring(7);
		
		return profileService.searchFollower(searchName, accessToken);
	}
		
	//내가 팔로우 하는 사람의 목록
	@GetMapping("/searchFollowing")
	public ResponseEntity<?> searchFollowing(@RequestParam(value = "searchName", required = false)String searchName , HttpServletRequest request)
	{
		log.info("----/profile/searchFollowing API 진입----");
		if(searchName == null)
			searchName ="";
		
		String accessToken = request.getHeader("Authorization").substring(7);

		return profileService.searchFollowing(searchName, accessToken);
	}
}
