package com.example.tutoring.service;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.tutoring.dto.MemberDto;
import com.example.tutoring.entity.Member;
import com.example.tutoring.jwt.JwtTokenProvider;
import com.example.tutoring.repository.MemberRepository;

@Slf4j
@Service
public class ProfileService {
		
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	UploadService uploadService;
	
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
	
}
