package com.example.tutoring.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.example.tutoring.service.AlimService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/alim")
public class AlimController {

	@Autowired
	AlimService alimService;
	
	@GetMapping(value="/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter subscribe(@RequestParam("memberNum") Integer memberNum) {
		return alimService.subscribe(memberNum);
	}
			
	@PatchMapping("/read")
	public ResponseEntity<Map<String,Object>> read(@RequestBody Map<String, Object> alimData)
	{
		return alimService.read(alimData);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String,Object>> delete(@RequestParam("alimNum") int alimNum)
	{
		return alimService.delete(alimNum);
	}
}
