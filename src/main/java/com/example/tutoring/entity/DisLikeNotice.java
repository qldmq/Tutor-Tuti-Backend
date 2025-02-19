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

import com.example.tutoring.dto.DisLikeNoticeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "dislike_notice")
public class DisLikeNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "dislikeNum", nullable = false, updatable = false)
    private Integer dislikeNum; 
    
    @Column(name="memberNum", nullable=false)
	private Integer memberNum;

    @Column(name="noticeNum", nullable=false)
	private Integer noticeNum;
    
    @Column(name = "dislikedAt", nullable = false)
    private Date dislikedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberNum", referencedColumnName = "memberNum", insertable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noticeNum", referencedColumnName = "noticeNum", insertable = false, updatable = false)
    private Notice notice;  
    
    public static DisLikeNotice toEntity(DisLikeNoticeDto dto)
    {
    	return DisLikeNotice.builder()
    			.dislikeNum(dto.getDislikeNum())
    			.memberNum(dto.getMemberNum())
    			.noticeNum(dto.getNoticeNum())
    			.dislikedAt(dto.getDislikedAt())
    			.build();
    }
}
