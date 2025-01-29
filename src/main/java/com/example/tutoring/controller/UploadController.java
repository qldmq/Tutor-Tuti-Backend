package com.example.tutoring.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.tutoring.service.UploadService;

@Controller
@RequestMapping("/image")
public class UploadController {
	
	@Autowired
	UploadService uploadService;

	@ResponseBody
	@PostMapping("/uploadProfileImg")
	public ResponseEntity<Map<String,Object>> upload(@RequestParam("image") MultipartFile file)
	{
		 return uploadService.uploadProfileImg(file);
	}

}
