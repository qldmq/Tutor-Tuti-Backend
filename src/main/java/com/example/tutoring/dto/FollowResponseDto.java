package com.example.tutoring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowResponseDto {
	private Integer memberNum;
	private String followNickname;
    private String followProfileImg;
    private String introduction;
}
