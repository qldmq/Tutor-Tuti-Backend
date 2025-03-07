package com.example.tutoring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class RoomService {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	public ResponseEntity<Map<String,Object>> createRoom(Map<String,Object> participants)
	{
		Map<String, Object> responseData = new HashMap<String, Object>(); 
								
		try {									
			List<Integer>partiMemberNumList = ((List<?>) participants.get("participantList"))
                    .stream()
                    .map(num -> Integer.parseInt(num.toString()))
                    .collect(Collectors.toList());
					
			ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
			Long roomId = valueOps.increment("roomIdSeq",1);
			
			String roomKey = "room:"+roomId;
			
			HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
			for(Integer memberNum : partiMemberNumList) {
				hashOps.put(roomKey, memberNum, false);
			}
			
			log.info("새로운 방 생성 : {}, 참가자: {}", roomId, partiMemberNumList);
			responseData.put("roomId", roomId);
			
			return ResponseEntity.status(HttpStatus.OK).body(responseData);
		}catch(Exception e)
		{
			responseData.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
		}
		
	}
	
	
	
}
