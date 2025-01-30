package com.example.tutoring.service;

import com.example.tutoring.dto.MemberDto;
import com.example.tutoring.dto.RefreshTokenDto;
import com.example.tutoring.entity.Member;
import com.example.tutoring.entity.RefreshToken;
import com.example.tutoring.jwt.JwtTokenProvider;
import com.example.tutoring.repository.MemberRepository;
import com.example.tutoring.repository.RefreshTokenRespository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    		Member member = memberRepository.findByMemberId(memberId);
        	
        	if(!passwordEncoder.matches(password, member.getPassword())) {
        		responseMap.put("message", "아이디와 패스워드가 일치하지 않습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
        	}
        	    	
        	String memberNumString = Integer.toString(member.getMemberNum());
        	
        	String accessToken = jwtTokenProvider.createAccessToken(memberNumString);
            String refreshToken = jwtTokenProvider.createRefreshToken(memberNumString);           
                     
             responseMap.put("access", accessToken);
             responseMap.put("member",member);
             return ResponseEntity.status(HttpStatus.OK).body(responseMap);
        	
    	}catch(Exception e)
    	{
    		responseMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
    	}
    
    }
    
    
    
}
