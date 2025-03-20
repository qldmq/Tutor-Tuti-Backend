package com.example.tutoring.config;


import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.example.tutoring.dto.ChattingDto;
import com.example.tutoring.jwt.JwtTokenProvider;
import com.example.tutoring.repository.MemberRepository;
import com.example.tutoring.type.ChattingType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompEventListener{
	
	private final SimpMessagingTemplate simpMessageingTemplate;	
	private final JwtTokenProvider jwtTokenProvider;
	public static final Map<Integer, Set<String>> roomSessions = new ConcurrentHashMap<>();

	@Autowired
	MemberRepository memberRepository;
	
	@EventListener
	public void handleSubscribeEvent(SessionSubscribeEvent event)
	{
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		
		if(accessor.getCommand() == StompCommand.SUBSCRIBE) {
			 String destination = accessor.getDestination(); 
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
				  
				  String sessionId = accessor.getSessionId();
				  roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(sessionId);
				  log.info("사용자 구독: sessionId={}, roomId={}", sessionId, roomId);
		     }
				     		     		     
		}
	}
	
	@EventListener
    public void handleUnsubscribeEvent(SessionUnsubscribeEvent event) {
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

	    String destination = accessor.getDestination(); 
	    log.info("사용자가 채널 구독 해제: " + destination);
		
	    String accessToken = accessor.getFirstNativeHeader("Authorization").substring(7);
	    log.info("받은 Access Token: " + accessToken);
	    
	    if (destination != null && destination.startsWith("/sub/")) {
	    	Integer roomId = Integer.parseInt(destination.replace("/sub/", ""));
			String nickname = memberRepository.findNicknameByMemberNum(Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken))).get().getNickname();

	        simpMessageingTemplate.convertAndSend(destination, getOutMessage(roomId, nickname));
	        String sessionId = accessor.getSessionId();
	        
	        roomSessions.forEach((entryRoomId, sessions) -> {
	            if (sessions.remove(sessionId)) {
	                log.info("사용자 구독 취소: sessionId={}, roomId={}", sessionId, entryRoomId);
	            }
	        });
	    }
	}
	
	private ChattingDto getOutMessage(Integer roomId, String nickname)
	{
		ChattingDto outMessage = new ChattingDto();
        outMessage.setRoomId(roomId);
        outMessage.setNickname(nickname);
        outMessage.setContent(nickname + "님이 방에서 나갔습니다.");
        outMessage.setType(ChattingType.TYPE_OUT);
        
        return outMessage;
	}
	
	public void handleDeleteChatRoom(Integer roomId) {
	    log.info("STOMP 채팅방 삭제됨: {}", roomId);
	    
	    if (roomSessions.containsKey(roomId)) {
	        roomSessions.remove(roomId);
	        log.info("STOMP 채팅방 및 모든 세션 제거됨: {}", roomId);
	    }
	}
}
