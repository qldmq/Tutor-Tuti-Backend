package com.example.tutoring.service;

import com.example.tutoring.dto.MemberDto;
import com.example.tutoring.dto.RefreshTokenDto;
import com.example.tutoring.entity.Member;
import com.example.tutoring.entity.RefreshToken;
import com.example.tutoring.jwt.CustomUserDetails;
import com.example.tutoring.jwt.JwtTokenProvider;
import com.example.tutoring.repository.MemberRepository;
import com.example.tutoring.repository.RefreshTokenRespository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@AllArgsConstructor
public class MemberService {
	
	@Autowired
    private AuthenticationManager authenticationManager;
	

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    // 회원가입 처리
    public ResponseEntity<Map<String, Object>> signUp(Map<String, Object> memberData) {

        String newNick = createNick();
        Map<String, Object> responseMap = new HashMap<>();

        try {
            // 중복 아이디가 있을 경우
            if (checkMemberId(memberData.get("memberId").toString())) {              
                responseMap.put("message", "이미 존재하는 아이디입니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
            }

            MemberDto memberDto = MemberDto.builder()
                    .loginType(0)
                    .memberId(memberData.get("memberId").toString())
                    .email(memberData.get("email").toString())
                    .password(passwordEncoder.encode(memberData.get("password").toString()))
                    .nickname(newNick)
                    .introduction("안녕하세요")  // DB 설정대로 출력되도록 수정 필요
                    .build();

            Member member = Member.toEntity(memberDto);
            memberRepository.save(member);
            return ResponseEntity.status(HttpStatus.OK).body(responseMap);
        } catch (Exception e) {
            responseMap.put("messsage", e.getMessage());          
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
        }

        
    }

    // 아이디 중복 확인
    public boolean checkMemberId(String memberId) {

        return memberRepository.existsByMemberId(memberId);
    }

    // 닉네임 생성
    public String createNick() {

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        Random random = new Random();
        int randNick =  1000 + random.nextInt(9000);

        return now.format(formatter) + randNick;
    }
    
    
    //로그인
    public ResponseEntity<Map<String,Object>>login(Map<String, Object> loginData)
    {
    	Map<String,Object> responseMap = new HashMap<String, Object>();
    	
    	String memberId = loginData.get("memberId").toString();
    	String password = loginData.get("password").toString();
    	
    	try {
    		    		
    		UsernamePasswordAuthenticationToken authenticationToken = 
    	            new UsernamePasswordAuthenticationToken(memberId, password);
	        Authentication authentication = authenticationManager.authenticate(authenticationToken);
    		
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	             
	        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
	        Member member = customUserDetails.getMember();
	        	               	
        	if(!passwordEncoder.matches(password, member.getPassword())) {
        		responseMap.put("message", "아이디와 패스워드가 일치하지 않습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
        	}
        	    	
        	String memberNumString = Integer.toString(member.getMemberNum());
        	
        	String accessToken = jwtTokenProvider.createAccessToken(memberNumString);
            String refreshToken = jwtTokenProvider.createRefreshToken(memberNumString);           
             
            
             responseMap.put("memberNum", member.getMemberNum());
             responseMap.put("loginType", member.getLoginType());
             responseMap.put("nickname", member.getNickname());
             responseMap.put("profileImg", member.getProfileImg());
             responseMap.put("introduction", member.getIntroduction());
             responseMap.put("access", accessToken);
             
             
             return ResponseEntity.status(HttpStatus.OK).body(responseMap);
        	
    	}catch(Exception e)
    	{
    		responseMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
    	}
    
    }
    
    //엑세스 토큰 체크
    public ResponseEntity<Map<String,Object>> accessCheck(String accessToken)
    {
    	Map<String,Object> responseMap = new HashMap<String,Object>();
    	
    	try {
    		
    		Map<String,Object> result = jwtTokenProvider.isAccessTokenExpired(accessToken);
    		int check = (int)result.get("check");
    		//엑세스 토큰이 만료되었을 경우
        	if(check == 0)
        	{
        		responseMap.put("check", 0);
        		return ResponseEntity.status(HttpStatus.OK).body(responseMap);
        	}
        	//엑세스 토큰이 만료되지 않았을 경우
        	else if(check == 1) {
        		responseMap.put("check", 1);
        		return ResponseEntity.status(HttpStatus.OK).body(responseMap);
        	}  	   
        	else{
        		responseMap.put("message", "유효하지 않은 토큰입니다.");
        		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
        	}
        	
    	}catch(Exception e)
    	{
    		responseMap.put("message", e.getMessage());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
    	}    	   	    	
    	
    }
    
    //토큰 재발급
    public ResponseEntity<Map<String,Object>> reissue(String accessToken)
    {
    	Map<String,Object> responseMap = new HashMap<String, Object>();
    	   	
    	try {
    		int memberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));   		
    		responseMap.put("access", jwtTokenProvider.reissueAccessToken(memberNum));
    		return ResponseEntity.status(HttpStatus.OK).body(responseMap);
    		
    	}catch(Exception e)
    	{
    		responseMap.put("message",e.getMessage());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
    	}
    	   	
    }
    
}
