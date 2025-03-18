package com.example.tutoring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.example.tutoring.dto.ChattingDto;
import com.example.tutoring.jwt.JwtTokenProvider;
import com.example.tutoring.repository.MemberRepository;
import com.example.tutoring.service.ChattingService;
import com.example.tutoring.type.ChattingType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompSubscribeEvent {

	private final SimpMessagingTemplate simpMessageingTemplate;	
	private final JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	ChattingService chattingService;
	
	@Autowired
	MemberRepository memberRepository;
	
	@EventListener
	public void handleSubscribeEvent(SessionSubscribeEvent event)
	{
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		
		if(accessor.getCommand() == StompCommand.SUBSCRIBE) {
			 String destination = accessor.getDestination(); // 구독한 채널
		     log.info("사용자가 채널 구독: " + destination);		     
		     String accessToken = accessor.getFirstNativeHeader("Authorization").substring(7);
		    		
		     if (destination.startsWith("/sub/")) {
		          Integer roomId = Integer.parseInt(destination.replace("/sub/", ""));
				  String nickname = memberRepository.findNicknameByMemberNum(Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken))).get().getNickname();

				  ChattingDto chattingDto = new ChattingDto();
				  chattingDto.setRoomId(roomId);
				  chattingDto.setNickname(nickname);
				  chattingDto.setContent(nickname+"님이 방에 참여했습니다.");
				  chattingDto.setType(ChattingType.TYPE_IN);
		    	 
				  simpMessageingTemplate.convertAndSend(destination, chattingDto);
		     }
		
		}
	}
	
}
