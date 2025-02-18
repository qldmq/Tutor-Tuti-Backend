package com.example.tutoring.service;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.tutoring.dto.FollowDto;
import com.example.tutoring.dto.FollowResponseDto;
import com.example.tutoring.dto.MemberDto;
import com.example.tutoring.entity.Follow;
import com.example.tutoring.entity.Member;
import com.example.tutoring.jwt.JwtTokenProvider;
import com.example.tutoring.repository.FollowRepository;
import com.example.tutoring.repository.MemberRepository;

@Slf4j
@Service
public class ProfileService {
		
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	UploadService uploadService;
	
	@Autowired
	FollowRepository followRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	public ResponseEntity<Map<String,Object>> profileImgUpdate(MultipartFile file , String accessToken)
	{
		Map<String,Object> responseMap = new HashMap<String, Object>();
		
		try {
			int memberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));
			Optional<Member> member = memberRepository.findById(memberNum);
			MemberDto dto = MemberDto.toDto(member.get());
			
			Map<String,Object> result = uploadService.uploadProfileImg(file);
			
			if((int)result.get("status") == 200)
			{
				dto.setProfileImg(result.get("url").toString());			
				memberRepository.save(Member.toEntity(dto));
				responseMap.put("profileImg", dto.getProfileImg());
				return ResponseEntity.status(HttpStatus.OK).body(responseMap);
			}
			else {
				log.info("이미지 업로드 실패");
				responseMap.put("message", "이미지 업로드 실패");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
			}								
		} catch(Exception e)
		{
			log.info(e.getMessage());
			responseMap.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
		}
		
	}
	
	public ResponseEntity<Map<String,Object>> followClick(String followerNickName, String accessToken)
	{
		Map<String,Object> responseMap = new HashMap<String, Object>();
		try {
			int memberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));
			Optional<Member> me = memberRepository.findById(memberNum);
			Optional<Member> follower = memberRepository.findByNickname(followerNickName);
			
			if(followRepository.followCheck(me.get().getMemberNum(), follower.get().getMemberNum()) > 0)
			{
				responseMap.put("message", "이미 팔로우한 회원입니다.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
			}else {			
				FollowDto followDto = FollowDto.builder()
						.followerMemberId(me.get().getMemberId())
						.followerMemberNum(memberNum)
						.followingMemberId(follower.get().getMemberId())
						.followingMemberNum(follower.get().getMemberNum())
						.build();
				followRepository.save(Follow.toEntity(followDto));			
				log.info(me.get().getNickname()+" 가 "+followerNickName+"를 팔로우");
				
				return ResponseEntity.status(HttpStatus.OK).body(responseMap);			
			}
									
		}catch(Exception e)
		{
			log.info(e.getMessage());
			responseMap.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);			
		}
		
	}
	
	public ResponseEntity<?> myFollower(String accessToken)
	{				
		try {
			int memberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));			
			List<FollowResponseDto> followerList = followRepository.findFollowerMemberList(memberNum);			
			return ResponseEntity.status(HttpStatus.OK).body(followerList);
		}catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
	}
	
	public ResponseEntity<?> myFollowing(String accessToken)
	{	
		try {
			int memberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));
			List<FollowResponseDto> followingList = followRepository.findFollowingMemberList(memberNum);
			return ResponseEntity.status(HttpStatus.OK).body(followingList);
		}catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
	}
	
	public ResponseEntity<Map<String,Object>> unFollow(String followerNickName, String accessToken)
	{
		Map<String,Object> responseMap = new HashMap<String, Object>();
		try {
			int memberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));
			Optional<Member> me = memberRepository.findById(memberNum);
			Optional<Member> follower = memberRepository.findByNickname(followerNickName);
			
			int myMemberNum = me.get().getMemberNum();
			int followingMemberNum = follower.get().getMemberNum();
			
			followRepository.unFollowMember(myMemberNum, followingMemberNum);			
			log.info(me.get().getNickname()+"가 "+follower.get().getNickname()+"를 언팔로우");
			responseMap.put("message", "언팔로우 성공");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);			
		}catch(Exception e)
		{
			log.info(e.getMessage());
			responseMap.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);			
		}
	}
		
	public ResponseEntity<?> searchFollower(String searchName, String accessToken)
	{
		try {
			int memberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));	
			searchName+="%";
			List<FollowResponseDto> followerList = followRepository.findSearchFollowerMemberList(memberNum, searchName);		
			return ResponseEntity.status(HttpStatus.OK).body(followerList);
		}catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	public ResponseEntity<?> searchFollowing(String searchName, String accessToken)
	{
		try {
			int memberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));	
			searchName+="%";
			List<FollowResponseDto> followingList = followRepository.findSearchFollowingMemberList(memberNum, searchName);			
			return ResponseEntity.status(HttpStatus.OK).body(followingList);
		}catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
