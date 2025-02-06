package com.example.tutoring.oauth2;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.tutoring.entity.Member;

public class CustomerOAuth2User implements OAuth2User{
	
	private OAuth2User oAuth2User;
	private String token;
	private Member member;
	
	public CustomerOAuth2User(OAuth2User oAuth2User, String  token, Member member)
	{
		this.oAuth2User = oAuth2User;
		this.token = token;
		this.member = member;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oAuth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getName() {
		return oAuth2User.getName();
	}
	
	public String getToken()
	{
		return token;
	}
		
    public Member getMember() {
        return member;
    }

}
