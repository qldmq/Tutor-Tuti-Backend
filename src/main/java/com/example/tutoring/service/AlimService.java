package com.example.tutoring.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.tutoring.dto.AlimDto;
import com.example.tutoring.entity.Alim;
import com.example.tutoring.repository.AlimRepository;
import com.example.tutoring.type.AlimType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlimService {
	
	@Autowired
	private AlimRepository alimRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<Integer, SseEmitter>();
	
	public SseEmitter subscribe(Integer memberNum) {
		SseEmitter emitter = new SseEmitter(60_000L);
		emitters.put(memberNum, emitter);
		
		emitter.onCompletion(() -> emitters.remove(memberNum));
		emitter.onTimeout(() -> emitters.remove(memberNum));
		
		return emitter;
	}
	
	public void sendAlim(Integer memberNum, String alimMsg, AlimType alimType) {			
		AlimDto alimDto = AlimDto.builder()
						.memberNum(memberNum)
						.alimMsg(alimMsg)
						.alimType(alimType)
						.sendTime(new Date())
						.isRead(false)
						.build();
		
		alimRepository.save(Alim.toEntity(alimDto));
		
		if(emitters.containsKey(memberNum)) {
			SseEmitter emitter = emitters.get(memberNum);
			
			try {
				emitter.send(SseEmitter.event().data(alimDto));
			} catch(IOException e) {
				emitters.remove(memberNum);
			}
		}
	}

	public ResponseEntity<Map<String,Object>> read(Map<String,Object> alimData)
	{
		Map<String,Object> response = new HashMap<String, Object>();
		
		try {
			int alimNum = (int)alimData.get("alimNum");
			alimRepository.readAlim(new Date(), true, alimNum);
			entityManager.flush();
			entityManager.clear();
	
			Optional<Alim> updateAlim = alimRepository.findById(alimNum);
			response.put("alim", AlimDto.toDto(updateAlim.get()));
			
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}catch(Exception e)
		{
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
								
	}
	
	public ResponseEntity<Map<String,Object>> delete(int alimNum)
	{
		Map<String,Object> response = new HashMap<String, Object>();
		
		try {
			alimRepository.deleteById(alimNum);
			
			response.put("message", "success");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch(Exception e)
		{
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
}
