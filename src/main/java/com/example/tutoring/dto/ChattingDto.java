package com.example.tutoring.dto;



import java.util.Date;
import com.example.tutoring.type.ChattingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChattingDto {

	private Integer roomId;
	private String nickname;
	private String content;
	private String profileImg;
	private ChattingType type;
	private Date sendTime;
	
}
