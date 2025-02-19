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
	
	//회원을 팔로우하는 사람의 목록
	@GetMapping("/followerList")
	public ResponseEntity<?> getFollowerList(@RequestParam("memberNum")int memberNum, @RequestParam(value = "observer", required = false) Integer observer)
	{
		log.info("----/profile/followerList API 진입----");			
		if(observer == null)
			observer = 0;
		
		return profileService.getFollowerList(memberNum, observer);
	}
	
	//회원이 팔로잉하는 사람의 목록
	@GetMapping("/followingList")
	public ResponseEntity<?> geyFollowingList(@RequestParam("memberNum")int memberNum,  @RequestParam(value = "observer", required = false) Integer observer)
	{
		log.info("----/profile/followingList API 진입----");		
		if(observer == null)
			observer = 0;
		
		return profileService.getFollowingList(memberNum, observer);
	}
	
	@DeleteMapping("/unFollow")
	public ResponseEntity<Map<String,Object>> unFollow(@RequestBody Map<String,Object> followData, HttpServletRequest request)
	{
		log.info("----/profile/unfollow API 진입----");
		String accessToken = request.getHeader("Authorization").substring(7);
		int followMemberNum = (int)followData.get("followMemberNum");;
		
		return profileService.unFollow(followMemberNum, accessToken);
	}
	
	//나를 팔로우 하는 사람의 목록
	@GetMapping("/searchFollower")
	public ResponseEntity<?> searchFollower(@RequestParam(value = "searchName", required = false)String searchName ,@RequestParam("memberNum")int memberNum, 
			@RequestParam(value = "observer", required = false) Integer observer)
	{
		log.info("----/profile/searchFollower API 진입----");
		if(searchName == null)
			searchName ="";		
		
		if(observer == null)
			observer = 0;
		
		
		return profileService.searchFollower(searchName, memberNum, observer);
	}
		
	//내가 팔로우 하는 사람의 목록
	@GetMapping("/searchFollowing")
	public ResponseEntity<?> searchFollowing(@RequestParam(value = "searchName", required = false)String searchName ,@RequestParam("memberNum")int memberNum,
			@RequestParam(value = "observer", required = false) Integer observer)
	{
		log.info("----/profile/searchFollowing API 진입----");
		if(searchName == null)
			searchName ="";
		
		if(observer == null)
			observer = 0;
		
			
		return profileService.searchFollowing(searchName, memberNum, observer);
	}
	
	
	@DeleteMapping("/deleteFollow")
	public ResponseEntity<Map<String,Object>> delteFollow(@RequestBody Map<String,Object> followData, HttpServletRequest request)
	{
		log.info("----/profile/deleteFollow API 진입----");
		String accessToken = request.getHeader("Authorization").substring(7);
		int followMemberNum = (int)followData.get("followMemberNum");
		
		return profileService.deleteFollow(followMemberNum, accessToken);
	}
}
