package com.example.tutoring.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompSubscribeEvent {

	private final SimpMessagingTemplate simpMessageingTemplate;
//	
//	@EventListener
//	public void handleSubscribeEvent(SesssionSubsc)
//	{
//		
//	}
	
}
