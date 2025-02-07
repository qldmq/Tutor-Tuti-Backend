package com.example.tutoring.oauth2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.example.tutoring.entity.Member;
import com.example.tutoring.jwt.JwtTokenProvider;
import com.example.tutoring.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> responseMap = (Map<String, Object>) attributes.get("response");

        // 네이버에서 가져온 사용자 정보
        String email = (String) responseMap.get("email");
        Member member = memberRepository.findByMemberId(email);

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(Integer.toString(member.getMemberNum()));
        jwtTokenProvider.createRefreshToken(Integer.toString(member.getMemberNum()));
        
        // 응답 데이터 구성
        Map<String, Object> responseData = new HashMap<>();
        
        responseData.put("memberNum", member.getMemberNum());
        responseData.put("loginType", member.getLoginType());
        responseData.put("nickname", member.getNickname());
        responseData.put("profileImg", member.getProfileImg());
        responseData.put("introduction", member.getIntroduction());
        responseData.put("access", accessToken);
        responseData.put("hasNotice", false);

        // JSON 형태로 응답
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }
}
