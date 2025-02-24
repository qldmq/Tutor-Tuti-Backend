package com.example.tutoring.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.tutoring.dto.MemberDto;
import com.example.tutoring.entity.Member;
import com.example.tutoring.jwt.JwtTokenProvider;
import com.example.tutoring.repository.FollowRepository;
import com.example.tutoring.repository.MemberRepository;
import com.example.tutoring.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuthService {
	
	@Autowired
	private RestTemplate restTemplate;
    
	@Autowired
	private MemberRepository memberRepository;
    
	@Autowired
	private FollowRepository followRepository;
	
	@Autowired
	private NoticeRepository noticeRepository;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Value("${spring.security.oauth2.client.registration.naver.client-id}")
	private String clientId;
	
	@Value("${spring.security.oauth2.client.registration.naver.client-secret}")
	private String clientSecret;

	@Value("${kakao.client-id}")
	private String kakaoClientId;

	@Value("${kakao.redirect-uri}")
	private String kakaoRedirectUri;

	@Value("${kakao.client-secret}")
	private String kakaoClientSecret;

	private final String TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
	private final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";


	public ResponseEntity<Map<String,Object>> naverLogin(Map<String,Object> loginData)
	{
		Map<String,Object> responseMap = new HashMap<String, Object>();
		
		int followerCnt = 0;
		int followCnt = 0;
		int noticeCnt = 0;
		
		try {
			String code = loginData.get("code").toString();
	        String state = loginData.get("state").toString();
	        
	        
	        String tokenUrl = "https://nid.naver.com/oauth2.0/token" +
	                "?grant_type=authorization_code" +
	                "&client_id="+clientId +
	                "&client_secret="+clientSecret+
	                "&code=" + code +
	                "&state=" + state;
	        
	        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, null, Map.class);
	        String naverAccessToken = (String) tokenResponse.getBody().get("access_token");
			
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "Bearer " + naverAccessToken);
	        HttpEntity<String> entity = new HttpEntity<>(headers);

	        ResponseEntity<Map> userResponse = restTemplate.exchange(
	                "https://openapi.naver.com/v1/nid/me",
	                HttpMethod.GET,
	                entity,
	                Map.class
	        );
	        
	        Map<String, Object> naverUser = (Map<String, Object>) userResponse.getBody().get("response");
	        String email = (String) naverUser.get("email");
	        
	        Member member = memberRepository.findByMemberId(email);
	        
	        if (member == null) {   	
	        	MemberDto memberDto = MemberDto.builder()
	                    .loginType(2)
	                    .memberId(email)
	                    .email(email)
	                    .password("naverPw")
	                    .nickname(createNick())
	                    .introduction("안녕하세요") 
	                    .build();
	        	       	
	        	member = memberRepository.saveAndFlush(Member.toEntity(memberDto));
	        }
			
	        String accessToken = jwtTokenProvider.createAccessToken(Integer.toString(member.getMemberNum()));
	        jwtTokenProvider.createRefreshToken(Integer.toString(member.getMemberNum()));
	                        
	        followerCnt = followRepository.followerCount(member.getMemberNum());
			followCnt = followRepository.followingCount(member.getMemberNum());
			noticeCnt = noticeRepository.noticeCount(member.getMemberNum());
	        
	        responseMap.put("memberNum", member.getMemberNum());
	        responseMap.put("loginType", member.getLoginType());
	        responseMap.put("nickname", member.getNickname());
	        responseMap.put("profileImg", member.getProfileImg());
	        responseMap.put("introduction", member.getIntroduction());
	        responseMap.put("access", accessToken);
	        responseMap.put("hasNotice", false);
	        responseMap.put("followCount", followCnt);
		    responseMap.put("followerCount", followerCnt);
			responseMap.put("noticeCount", noticeCnt);   
			
			
			return ResponseEntity.status(HttpStatus.OK).body(responseMap);
			
		}catch(Exception e)
		{			
			responseMap.put("message", e.getMessage());			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
		}

	}

	private String createNick() {

		LocalDate now = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		Random random = new Random();
		int randNick =  1000 + random.nextInt(9000);

		return now.format(formatter) + randNick;
		}


	// 카카오 로그인
	public ResponseEntity<Map<String, Object>> kakaoLogin(String code) {

		Map<String, Object> responseMap = new HashMap<>();
		
		int followerCnt = 0;
		int followCnt = 0;
		int noticeCnt = 0;

		try {
			// 1. 인가 코드로 액세스 토큰 요청
			String tokenUrl = TOKEN_REQUEST_URL +
					"?grant_type=authorization_code" +
					"&client_id=" + kakaoClientId +
					"&redirect_uri=" + kakaoRedirectUri +
					"&code=" + code +
					"&client_secret=" + kakaoClientSecret;

			ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, null, Map.class);
			String kakaoAccessToken = (String) tokenResponse.getBody().get("access_token");

			// 2. 액세스 토큰으로 사용자 정보 요청
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + kakaoAccessToken);
			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<Map> userResponse = restTemplate.exchange(
					USER_INFO_URL,
					HttpMethod.GET,
					entity,
					Map.class
			);

			Map<String, Object> kakaoAccount = (Map<String, Object>) userResponse.getBody().get("kakao_account");
			String email = (String) kakaoAccount.get("email");

			// 프로필 이미지 URL 가져오기
			String profileImgUrl = null;
			if (kakaoAccount.containsKey("profile") && kakaoAccount.get("profile") != null) {
				Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
				profileImgUrl = (String) profile.get("profile_image_url");
			}

			// 3. 사용자 정보 DB 저장 및 조회
			Member member = memberRepository.findByMemberId(email);

			if (member == null) {
				MemberDto memberDto = MemberDto.builder()
						.loginType(1)
						.memberId(email)
						.email(email)
						.password("kakaoPw")
						.nickname(createNick())
						.introduction("안녕하세요")
						.profileImg(profileImgUrl)
						.build();

				member = memberRepository.saveAndFlush(Member.toEntity(memberDto));
			}

			// 4. JWT 토큰 생성
			String accessToken = jwtTokenProvider.createAccessToken(Integer.toString(member.getMemberNum()));
			jwtTokenProvider.createRefreshToken(Integer.toString(member.getMemberNum()));
		
			followerCnt = followRepository.followerCount(member.getMemberNum());
			followCnt = followRepository.followingCount(member.getMemberNum());
			noticeCnt = noticeRepository.noticeCount(member.getMemberNum());
			
			// 5. 반환 데이터 설정
			responseMap.put("memberNum", member.getMemberNum());
			responseMap.put("loginType", member.getLoginType());
			responseMap.put("nickname", member.getNickname());
			responseMap.put("profileImg", member.getProfileImg());
			responseMap.put("introduction", member.getIntroduction());
			responseMap.put("access", accessToken);
			responseMap.put("hasNotice", false);
			responseMap.put("followCount", followCnt);
			responseMap.put("followerCount", followerCnt);
			responseMap.put("noticeCount", noticeCnt);
						
					
			return ResponseEntity.status(HttpStatus.OK).body(responseMap);

		} catch (Exception e) {
			responseMap.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
		}
	}
}
