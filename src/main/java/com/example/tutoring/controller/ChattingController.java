package com.example.tutoring.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.example.tutoring.dto.ChattingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChattingController {

	private final SimpMessagingTemplate simpMessagingTemplate;
	
	@MessageMapping("/{chattingRoomId}/messages")
	private void chat(@DestinationVariable("chattingRoomId") Integer roomId, ChattingDto chattingDto)
	{	
		simpMessagingTemplate.convertAndSend("/sub/"+roomId,chattingDto);
	}
	
}
