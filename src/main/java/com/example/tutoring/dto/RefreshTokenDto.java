package com.example.tutoring.dto;

import java.time.Instant;

import javax.persistence.Column;

import com.example.tutoring.entity.RefreshToken;

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
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {

	private Integer memberNum;

	private String reToken;
	
	private Instant expiryDate;
	
	public static RefreshTokenDto toDto(RefreshToken entity)
	{
		return RefreshTokenDto.builder()
				.memberNum(entity.getMemberNum())
				.reToken(entity.getReToken())
				.expiryDate(entity.getExpiryDate())
				.build();
	}
	
}
