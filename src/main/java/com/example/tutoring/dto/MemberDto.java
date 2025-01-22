package com.example.tutoring.dto;

import com.example.tutoring.entity.Member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberDto {

	private Integer member_num;

	private String email;

	private String password;

	private Integer login_type;

	private String phone;

	private String nickname;

	private String  profile_img;
	
	public static MemberDto toDto(Member entity)
	{
		return MemberDto.builder()
				.member_num(entity.getMember_num())
				.email(entity.getEmail())
				.password(entity.getPassword())
				.login_type(entity.getLogin_type())
				.phone(entity.getPhone())
				.nickname(entity.getNickname())
				.profile_img(entity.getProfile_img())
				.build();
	}
	
}
