package com.example.tutoring.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.tutoring.dto.ChattingDto;
import com.example.tutoring.type.ChattingType;
import com.example.tutoring.service.ChattingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChattingController {

	@Autowired
	private ChattingService chattingService;
			
	private final SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/{roomId}/messages")
	private void chat(@DestinationVariable("roomId") Integer roomId, ChattingDto chattingDto)
	{
		chattingDto.setRoomId(roomId);
		if(chattingDto.getType().equals(ChattingType.TYPE_IN))
		{
			log.info("방 참여");
			chattingDto.setContent(chattingDto.getNickname() + "님이 방에 참여했습니다.");			
		} else if (chattingDto.getType().equals(ChattingType.TYPE_OUT)) {
			log.info("방 나감");
			chattingDto.setContent(chattingDto.getNickname() + "님이 방을 나갔습니다.");
		} else if (chattingDto.getType().equals(ChattingType.TYPE_TEXT)) {
			log.info("텍스트 메시지");
		} else if(chattingDto.getType().equals(ChattingType.TYPE_IMG)) {
			log.info("이미지");
		}
		
		chattingService.insertChatting(chattingDto);
		simpMessagingTemplate.convertAndSend("/sub/"+roomId,chattingDto);
	}

	@PostMapping("/chattings/image")
	public ResponseEntity<Map<String,Object>> imgSend(@RequestParam("image") MultipartFile file)
	{
		return chattingService.imgSend(file);
	}	
		
	@GetMapping("/chattings/list")
	public ResponseEntity<Map<String,Object>> chattingList(@RequestParam("roomId")Integer roomId)
	{
		System.out.println(roomId);
		return chattingService.chattingList(roomId);
	}

}
