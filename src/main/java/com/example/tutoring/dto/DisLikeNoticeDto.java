package com.example.tutoring.dto;

import java.util.Date;

import com.example.tutoring.entity.DisLikeNotice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisLikeNoticeDto {
	
	private Integer dislikeNum;  
	private Integer memberNum;
	private Integer noticeNum;    
    private Date dislikedAt;  
    
    public static DisLikeNoticeDto toDto(DisLikeNotice entity)
    {
    	return DisLikeNoticeDto.builder()
    			.dislikeNum(entity.getDislikeNum())
    			.memberNum(entity.getMemberNum())
    			.noticeNum(entity.getNoticeNum())
    			.dislikedAt(entity.getDislikedAt())
    			.build();
    }
}
