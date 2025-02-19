package com.example.tutoring.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.tutoring.dto.LikeNoticeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "like_notice")
public class LikeNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "likeNum", nullable = false, updatable = false)
    private Integer likeNum; 
    
    @Column(name="memberNum", nullable=false)
	private Integer memberNum;

    @Column(name="noticeNum", nullable=false)
	private Integer noticeNum;
    
    @Column(name = "likedAt", nullable = false)
    private Date likedAt;  
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberNum", referencedColumnName = "memberNum", insertable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noticeNum", referencedColumnName = "noticeNum", insertable = false, updatable = false)
    private Notice notice;

    public static LikeNotice toEntity(LikeNoticeDto dto)
    {
    	return LikeNotice.builder()
    			.likeNum(dto.getLikeNum())
    			.memberNum(dto.getMemberNum())
    			.noticeNum(dto.getNoticeNum())
    			.likedAt(dto.getLikedAt())
    			.build();
    }
}
