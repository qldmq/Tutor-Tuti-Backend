package com.example.tutoring.dto;

import java.util.Date;
import com.example.tutoring.entity.Alim;
import com.example.tutoring.type.AlimType;
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
public class AlimDto {

	private Integer alimNum;
	private Integer memberNum;
	private String alimMsg;
	private AlimType alimType;
	private Date sendTime;
	private Date readTime;
	private boolean isRead;
	
	public static AlimDto toDto(Alim entity) {
		return AlimDto.builder()
				.alimNum(entity.getAlimNum())
				.memberNum(entity.getMemberNum())
				.alimMsg(entity.getAlimMsg())
				.alimType(entity.getAlimType())
				.sendTime(entity.getSendTime())
				.readTime(entity.getReadTime())
				.isRead(entity.isRead())
				.build();
	}
	
}
