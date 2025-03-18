package com.example.tutoring.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.example.tutoring.dto.AlimDto;
import com.example.tutoring.type.AlimType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="alim")
public class Alim {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name="alimNum", nullable=false)
	private Integer alimNum;
	
	@Column(name="memberNum", nullable=false)
	private Integer memberNum;
	
	@Column(name="alimMsg")
	private String alimMsg;
	
	@Enumerated(EnumType.STRING)
	@Column(name="alimType")
	private AlimType alimType;
	
	@Column(name="sendTime")
	private Date sendTime;

	@Column(name="readTime")
	private Date readTime;
	
	@Column(name="isRead")
	private boolean isRead;
	
	//알림타입이 LECTURE일 경우에만 사용
	@Column(name="roomId")
	private Integer roomId;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberNum", referencedColumnName = "memberNum", insertable = false, updatable = false)
    private Member member;

	public static Alim toEntity(AlimDto dto) {
		return Alim.builder()
				.alimNum(dto.getAlimNum())
				.memberNum(dto.getMemberNum())
				.alimMsg(dto.getAlimMsg())
				.alimType(dto.getAlimType())
				.sendTime(dto.getSendTime())
				.readTime(dto.getReadTime())
				.isRead(dto.isRead())
				.roomId(dto.getRoomId())
				.build();
	}
	
}
