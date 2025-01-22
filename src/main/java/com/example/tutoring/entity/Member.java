package com.example.tutoring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.example.tutoring.dto.MemberDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name="member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="member_num")
	private Integer member_num;
	
	@Column(name="email")
	private String email;
	
	@Column(name="password")
	private String password;
	
	@Column(name="login_type")
	private Integer login_type;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="nickname")
	private String nickname;
	
	@Column(name="profile_img")
	private String  profile_img;
	
	
	public static Member toEntity(MemberDto dto)
	{
		return Member.builder()
				.member_num(dto.getMember_num())
				.email(dto.getEmail())
				.password(dto.getPassword())
				.login_type(dto.getLogin_type())
				.phone(dto.getPhone())
				.nickname(dto.getNickname())
				.profile_img(dto.getProfile_img())
				.build();
	}
	
	
}
