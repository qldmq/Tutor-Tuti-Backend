package com.example.tutoring.config;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SignalingHandler extends TextWebSocketHandler {

    private final Map<Integer, Set<WebSocketSession>> signalingRooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("새로운 WebRTC 연결: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        JSONObject jsonMessage = new JSONObject(message.getPayload());
        String type = jsonMessage.getString("type");
        Integer roomId = jsonMessage.getInt("roomId");

        switch (type) {
            case "create_room":
                handleCreateRoom(roomId, session);
                break;
            case "join_room":
                handleJoinRoom(roomId, session);
                break;
            case "offer":
                handleOffer(roomId, session, jsonMessage);
                break;
            case "answer":
                handleAnswer(roomId, session, jsonMessage);
                break;
            case "candidate":
                handleCandidate(roomId, session, jsonMessage);
                break;
            case "leave":
                handleLeave(roomId, session);
                break;
        }
    }

    private void handleCreateRoom(Integer roomId, WebSocketSession session) {
        signalingRooms.putIfAbsent(roomId, ConcurrentHashMap.newKeySet());
        signalingRooms.get(roomId).add(session);
                               
        log.info("방 생성됨: {}", roomId);
    }

    private void handleJoinRoom(Integer roomId, WebSocketSession session) {
    	Set<WebSocketSession> sessions = signalingRooms.get(roomId);
        if (sessions == null) {
        	   log.error("방이 존재하지 않습니다: {}", roomId);
               try {
				session.sendMessage(new TextMessage("{\"error\": \"Room not found\"}"));
			} catch (IOException e) {
				e.printStackTrace();
			}
               return;
        }
        signalingRooms.putIfAbsent(roomId, ConcurrentHashMap.newKeySet());
        signalingRooms.get(roomId).add(session);        
        sessions = signalingRooms.get(roomId);
        
        String message = "{\"type\": \"new_member\", \"roomId\": " + roomId + ", \"sessionId\": \"" + session.getId() + "\"}";
        for (WebSocketSession s : sessions) {
            try {
                s.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        log.info("참여자 입장: {} - 방 ID: {}", session.getId(), roomId);
    }

    private void sendToRoom(Integer roomId, WebSocketSession sender, JSONObject message) throws IOException {
        log.info("{} 전송: {} -> 방 ID: {}", message.getString("type"), sender.getId(), roomId);

        Set<WebSocketSession> sessions = signalingRooms.get(roomId);
        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen() && !session.equals(sender)) {
                    session.sendMessage(new TextMessage(message.toString()));
                }
            }
        }
    }

    private void handleLeave(Integer roomId, WebSocketSession session) {
        Set<WebSocketSession> sessions = signalingRooms.get(roomId);
        if (sessions != null) {
            sessions.remove(session);
            log.info("사용자 퇴장: {} - 방 ID: {}", session.getId(), roomId);
            if (sessions.isEmpty()) {
                signalingRooms.remove(roomId);
                log.info("방 삭제됨 (참여자 없음): {}", roomId);
            }
        }
    }

    public void handleDeleteSignalingRoom(Integer roomId) throws IOException {
        log.info("WebRTC 방 삭제됨: {}", roomId);

        Set<WebSocketSession> sessions = signalingRooms.get(roomId);

        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                try {
                    if (session.isOpen()) {
                        session.close();
                    }
                } catch (IOException e) {
                    log.error("WebRTC 세션 종료 실패: {}", session.getId(), e);
                }
            }
        }
        
        
        signalingRooms.remove(roomId);
        log.info("WebRTC 방 및 모든 세션 제거됨: {}", roomId);
    }

    private void handleOffer(Integer roomId, WebSocketSession session, JSONObject message) throws IOException {
        log.info("Offer 전송: {} -> 방 ID: {}", session.getId(), roomId);
        sendToRoom(roomId, session, message); 
    }

    private void handleAnswer(Integer roomId, WebSocketSession session, JSONObject message) throws IOException {
        log.info("Answer 전송: {} -> 방 ID: {}", session.getId(), roomId);
        sendToRoom(roomId, session, message); 
    }

    private void handleCandidate(Integer roomId, WebSocketSession session, JSONObject message) throws IOException {
        log.info("Candidate 전송: {} -> 방 ID: {}", session.getId(), roomId);
        sendToRoom(roomId, session, message); 
    }
}
