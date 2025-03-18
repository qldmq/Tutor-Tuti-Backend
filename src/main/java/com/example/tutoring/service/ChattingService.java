package com.example.tutoring.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.tutoring.dto.ChattingDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChattingService {

	@Autowired
	UploadService uploadService;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public ResponseEntity<Map<String,Object>> imgSend(MultipartFile file)
	{
		Map<String,Object> responseMap = new HashMap<String, Object>();
		try {			
			return ResponseEntity.status(HttpStatus.OK).body(uploadService.uploadProfileImg(file));
		} catch(Exception e)
		{
			responseMap.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
		}		
	}
	
	public ResponseEntity<Map<String, Object>> chattingList(Integer roomId)
	{
		ListOperations<String, Object> listOps = redisTemplate.opsForList();
		List<Object> chatData = listOps.range("chat:"+roomId, 0, -1);
		Map<String,Object> responseMap = new HashMap<String, Object>();
		
		try {
			List<ChattingDto> chattingList =  chatData.stream().map(data ->{
				try {
					return objectMapper.readValue(data.toString(), ChattingDto.class);
				}catch(Exception e)
				{
					throw new RuntimeException("JSON 변환 오류");
				}
			}).collect(Collectors.toList());
			
			responseMap.put("chattingList", chattingList);
			return ResponseEntity.status(HttpStatus.OK).body(responseMap);
			
		}catch(Exception e)
		{
			responseMap.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
		}
											
	}
	
	
	public void insertChatting(ChattingDto chattingDto)
	{		
		ListOperations<String, Object> listOps = redisTemplate.opsForList();
		chattingDto.setSendTime(new Date());
		try {
			String chatJson = objectMapper.writeValueAsString(chattingDto);
			listOps.rightPush("chat:"+chattingDto.getRoomId(), chatJson);
			log.info("메시지 insert");
		} catch(JsonProcessingException e)
		{
			e.printStackTrace();
			log.info("Error Message : "+e.getMessage());
		}
	}
	
}
