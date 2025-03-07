package com.example.tutoring.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.tutoring.service.RoomService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {
	
	
	@Autowired
	RoomService roomService;

	@PostMapping("/create")
	public ResponseEntity<Map<String,Object>> createRoom(@RequestBody Map<String,Object> participants)
	{		
		return roomService.createRoom(participants);
	}
	
}
