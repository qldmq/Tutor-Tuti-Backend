package com.example.tutoring.service;

import com.example.tutoring.dto.NoticeDto;
import com.example.tutoring.entity.Notice;
import com.example.tutoring.repository.NoticeRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
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

	@Autowired
	NoticeRepository noticeRepository;

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

	public ResponseEntity<?> getFollowerList(int memberNum, Integer observer ,String accessToken)
	{
		try {
			Map<String,Object> response = new HashMap<String, Object>();
			int pageSize = 10;
			int offset = observer * pageSize;
			
			int myMemeberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));
			
			List<Object[]> result = followRepository.findFollowerMemberList(memberNum, pageSize, offset);
			List<FollowResponseDto> followerList = new ArrayList<>();
			for (Object[] obj : result) {
				Integer followMemberNum = (Integer) obj[0];
				String nickname = (String) obj[1];
				String profileImg = (String) obj[2];
				String introduction = (String) obj[3];
				boolean status = true;
				if(followRepository.followCheck(followMemberNum, myMemeberNum) < 1)
					status = false;
								
				followerList.add(new FollowResponseDto(followMemberNum, nickname, profileImg, introduction, status));
			}

			response.put("followList",followerList);
			
			if(followerList.size() < pageSize)
				response.put("flag", true);
			else
				response.put("flag", false);
			
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	public ResponseEntity<?> getFollowingList(int memberNum, Integer observer ,String accessToken)
	{
		try {

			Map<String,Object> response = new HashMap<String, Object>();
			int pageSize = 10;
			int offset = observer * pageSize;
			
			int myMemeberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));

			List<Object[]> result = followRepository.findFollowingMemberList(memberNum, pageSize, offset);

			List<FollowResponseDto> followingList = new ArrayList<>();
			for (Object[] obj : result) {
				Integer followMemberNum = (Integer) obj[0];
				String nickname = (String) obj[1];
				String profileImg = (String) obj[2];
				String introduction = (String) obj[3];
				
				boolean status = true;
				if(followRepository.followCheck(followMemberNum, myMemeberNum) < 1)
					status = false;

				followingList.add(new FollowResponseDto(followMemberNum, nickname, profileImg, introduction,status));
			}
			
			response.put("followList",followingList);
			
			if(followingList.size() < pageSize)
				response.put("flag", true);
			else
				response.put("flag", false);

			return ResponseEntity.status(HttpStatus.OK).body(response);
		}catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	public ResponseEntity<Map<String,Object>> unFollow(int followMemberNum, String accessToken)
	{
		Map<String,Object> responseMap = new HashMap<String, Object>();
		try {
			int myMemberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));
			followRepository.unFollowMember(myMemberNum, followMemberNum);
			responseMap.put("message", "언팔로우 성공");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
		}catch(Exception e)
		{
			log.info(e.getMessage());
			responseMap.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
		}
	}

	public ResponseEntity<?> searchFollower(String searchName, int memberNum, Integer observer,String accessToken)
	{
		
		Map<String,Object> response = new HashMap<String, Object>();
		
		try {
			searchName+="%";
			int pageSize = 10;
			int offset = observer * pageSize;

			int myMemeberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));
			
			List<Object[]> result = followRepository.findSearchFollowerMemberList(memberNum, searchName, pageSize, offset);
			List<FollowResponseDto> followerList = new ArrayList<>();
			for (Object[] obj : result) {
				Integer followMemberNum = (Integer) obj[0];
				String nickname = (String) obj[1];
				String profileImg = (String) obj[2];
				String introduction = (String) obj[3];
				
				boolean status = true;
				if(followRepository.followCheck(followMemberNum, myMemeberNum) < 1)
					status = false;
				
				followerList.add(new FollowResponseDto(followMemberNum, nickname, profileImg, introduction,status));
			}
			
			
			response.put("followList",followerList);
			
			if(followerList.size() < pageSize)
				response.put("flag", true);
			else
				response.put("flag", false);

			return ResponseEntity.status(HttpStatus.OK).body(response);
		}catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	public ResponseEntity<?> searchFollowing(String searchName, int memberNum, Integer observer,String accessToken)
	{
		
		Map<String,Object> response = new HashMap<String, Object>();
		
		try {
			searchName+="%";
			int pageSize = 10;
			int offset = observer * pageSize;
			
			int myMemeberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));

			List<Object[]> result = followRepository.findSearchFollowingMemberList(memberNum, searchName, pageSize, offset);
			List<FollowResponseDto> followingList = new ArrayList<>();
			for (Object[] obj : result) {
				Integer followMemberNum = (Integer) obj[0];
				String nickname = (String) obj[1];
				String profileImg = (String) obj[2];
				String introduction = (String) obj[3];
				
				boolean status = true;
				if(followRepository.followCheck(followMemberNum, myMemeberNum) < 1)
					status = false;
				
				followingList.add(new FollowResponseDto(followMemberNum, nickname, profileImg, introduction,status));
			}

			response.put("followList",followingList);
			
			if(followingList.size() < pageSize)
				response.put("flag", true);
			else
				response.put("flag", false);
						
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}


	public ResponseEntity<Map<String,Object>> deleteFollow(int followMemberNum, String accessToken)
	{
		Map<String,Object> responseMap = new HashMap<String, Object>();
		try {
			int myMemberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));
			followRepository.deleteFollowMember(followMemberNum,myMemberNum);

			responseMap.put("message", "팔로워 삭제 성공");
			return ResponseEntity.status(HttpStatus.OK).body(responseMap);
		}catch(Exception e)
		{
			log.info(e.getMessage());
			responseMap.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
		}
	}

	// 내가 작성한 공지글
	public ResponseEntity<Map<String, Object>> myNotice(String accessToken) {

		Map<String, Object> response = new HashMap<>();

		if (!jwtTokenProvider.validateToken(accessToken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		try {
			int memberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));

			List<Notice> notices = noticeRepository.findByMemberNum(memberNum);

			List<NoticeDto> noticeList = new ArrayList<>();

			for(Notice notice:notices) {
				noticeList.add(NoticeDto.toDto(notice));
			}

			response.put("notices", noticeList);
			return ResponseEntity.status(HttpStatus.OK).body(response);

		} catch (Exception e) {
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
	public ResponseEntity<Map<String, Object>> profileInfo(int memberNum)
	{
		Map<String,Object> response = new HashMap<String, Object>();
		
		int followerCnt = 0;
		int followCnt = 0;
		int noticeCnt = 0;
		
		try {
			Member member = memberRepository.findById(memberNum).get();
			followerCnt = followRepository.followerCount(memberNum);
			followCnt = followRepository.followingCount(memberNum);
			noticeCnt = noticeRepository.noticeCount(memberNum);
						
			response.put("memberNum", member.getMemberNum());
			response.put("nickname", member.getNickname());
			response.put("profileImg", member.getProfileImg());
			response.put("introduction",member.getIntroduction());
			response.put("followCount", followCnt);
			response.put("followerCnt", followerCnt);
			response.put("noticeCount", noticeCnt);
			
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}catch(Exception e)
		{
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
				
		
	}
	
}