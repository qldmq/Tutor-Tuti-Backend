package com.example.tutoring.dto;

import java.util.Date;

import com.example.tutoring.entity.LikeNotice;

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
public class LikeNoticeDto {
	
    private Integer likeNum;  
	private Integer memberNum;
	private Integer noticeNum;    
    private Date likedAt;  
    
    
    public static LikeNoticeDto toDto(LikeNotice entity)
    {
    	return LikeNoticeDto.builder()
    			.likeNum(entity.getLikeNum())
    			.memberNum(entity.getMemberNum())
    			.noticeNum(entity.getNoticeNum())
    			.likedAt(entity.getLikedAt())
    			.build();
    }
}
