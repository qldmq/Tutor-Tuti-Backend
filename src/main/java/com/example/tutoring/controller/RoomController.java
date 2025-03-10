package com.example.tutoring.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.tutoring.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {
		
	@Autowired
	RoomService roomService;

	@PostMapping("/create")
	public ResponseEntity<Map<String,Object>> createRoom(HttpServletRequest request , @RequestBody Map<String,Object> participants)
	{		
		String accessToken = request.getHeader("Authorization").substring(7);
		log.info("엑세스 토큰 : "+accessToken);
		return roomService.createRoom(accessToken,participants);
	}
		
	@GetMapping("/member")
	public  ResponseEntity<Map<String, Object>> getRoomMembers(@RequestParam("roomId") Long roomId) {
	    return roomService.getRoomMembers(roomId);
	}	
	
	@PatchMapping("/udpateInitStatus")
	public ResponseEntity<Map<String,Object>> updateInitStatus(HttpServletRequest request ,@RequestParam("roomId") Long roomId)
	{
		String accessToken = request.getHeader("Authorization").substring(7);
		log.info("엑세스 토큰 : "+accessToken);
		
		return roomService.updateInitStatus(accessToken, roomId);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String,Object>> deleteRoom(@RequestParam("roomId") Long roomId)
	{
		return roomService.deleteRoom(roomId);
	}
	
	
}
