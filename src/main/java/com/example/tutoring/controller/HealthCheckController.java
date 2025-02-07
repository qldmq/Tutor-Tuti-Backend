package com.example.tutoring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HealthCheckController {

	
	//AWS Load Balancer의 Health Check API
	@GetMapping("/")
	public ResponseEntity<String> healthCheck()
	{
		log.info("health check");
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}
	
}
