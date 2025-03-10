package com.example.tutoring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.tutoring.jwt.JwtTokenProvider;
import com.example.tutoring.repository.MemberRepository;
import com.example.tutoring.type.AlimType;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class RoomService {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private AlimService alimService;
	
	@Transactional
	public ResponseEntity<Map<String,Object>> createRoom(String accessToken, Map<String,Object> participants)
	{
		Map<String, Object> responseData = new HashMap<String, Object>(); 
								
		try {												
			//방 생성자 - 호스트
			Integer hostMemberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));
			
			//방 참여자 - 클라이언트로 부터 받은 팔로워 리스트
			List<Integer>partiMemberNumList = ((List<?>) participants.get("participantList"))
                    .stream()
                    .map(num -> Integer.parseInt(num.toString()))
                    .collect(Collectors.toList());
					
		
			ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
			
			//roomId 생성
			Long roomId = valueOps.increment("roomIdSeq",1);
			String roomKey = "room:"+roomId;
			
			HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
			hashOps.put(roomKey, "hostMemberNum", hostMemberNum);
			
			//받은 참여자에 본인의 memberNum추가
			partiMemberNumList.add(hostMemberNum);
			
			//참여자 리스트 redist에 저장
			for(Integer memberNum : partiMemberNumList) {
				
				if(memberNum == hostMemberNum)
				{
					hashOps.put(roomKey, memberNum, true);
				}
				else {
					hashOps.put(roomKey, memberNum, false);	
					
					//참여자들에게 알림 발송
					alimService.sendAlim(memberNum,memberRepository.findNicknameByMemberNum(hostMemberNum).get().getNickname(), AlimType.TYPE_LECTURE);
				}				
			}
			
			log.info("새로운 방 생성 : {}, 참가자: {}", roomId, partiMemberNumList);
			responseData.put("roomId", roomId);		
			
			//생성된 방의 참여자 리스트 추출
	    	HashOperations<String, Object, Object> hashOpsResult = redisTemplate.opsForHash();
		    Map<Object, Object> membersMap = hashOpsResult.entries(roomKey);
		    
		    List<Map<String, Object>> participantList = membersMap.entrySet().stream()
		    	    .filter(entry -> !entry.getKey().toString().equals("hostMemberNum"))
		    	    .map(entry -> {
		    	        Map<String, Object> member = new HashMap<>();
		    	        int memberNum = Integer.parseInt(entry.getKey().toString());
		    	        member.put("memberNum", memberNum);
		    	        member.put("nickname", memberRepository.findNicknameByMemberNum(memberNum).get().getNickname());
		    	        member.put("initStatus", entry.getValue());
		    	        return member;
		    	    })
		    	    .collect(Collectors.toList());

		    responseData.put("hostMemberNum", membersMap.get("hostMemberNum"));
		    responseData.put("participantList", participantList);
						
			return ResponseEntity.status(HttpStatus.OK).body(responseData);
		}catch(Exception e)
		{
			responseData.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
		}		
	}
	
	
	public ResponseEntity<Map<String, Object>> getRoomMembers(Long roomId) {	    
		String roomKey = "room:" + roomId;
	    Map<String, Object> responseData = new HashMap<>();
		
	    try {
	    	HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
		    Map<Object, Object> membersMap = hashOps.entries(roomKey);
		    
		    List<Map<String, Object>> participantList = membersMap.entrySet().stream()
	    		.filter(entry -> !entry.getKey().toString().equals("hostMemberNum"))
		        .map(entry -> {
		            Map<String, Object> member = new HashMap<>();
		            int memberNum = Integer.parseInt(entry.getKey().toString());
		            member.put("memberNum", memberNum);
		            member.put("nickname",memberRepository.findNicknameByMemberNum(memberNum).get().getNickname());
		            member.put("initStatus", entry.getValue());
		            return member;
		        })
		        .collect(Collectors.toList());

		    responseData.put("roomId", roomId);	
		    responseData.put("hostMemberNum", membersMap.get("hostMemberNum"));
		    responseData.put("participantList", participantList);
		    return ResponseEntity.status(HttpStatus.OK).body(responseData);
	    } catch(Exception e)
	    {
	    	responseData.put("message", e.getMessage());
		    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
	    }    
	}
	
	//방 참여, 나감 처리
	public ResponseEntity<Map<String,Object>> updateInitStatus(String accessToken, Long roomId)
	{
	   Map<String, Object> responseData = new HashMap<>();
	   
	   try {
		   String roomKey = "room:" + roomId;
		   HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
		   
		   Integer memberNum = Integer.parseInt(jwtTokenProvider.getMemberNum(accessToken));
		   		   
		   if(!hashOps.hasKey(roomKey, memberNum)) {
			   responseData.put("message", "해당 멤버가 존재하지 않습니다.");
			   return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
		   }
		   
		   boolean currentStatus = Boolean.parseBoolean(hashOps.get(roomKey, memberNum).toString());
		   
		   //true일 경우에 이 메서드를 호출했으면 방을 나가는 것 이므로 false로 변경
		   //false일 경우에 이 메서드를 호출했으면 방에 참여한 것 이므로 true로 변경
		   boolean newStatus = !currentStatus;
		   hashOps.put(roomKey, memberNum, newStatus);
		  
		   if(newStatus)
		   {
			   log.info("방 [{}]의 멤버 [{}] 참여", roomId, memberNum);
		   }
		   else {
			   log.info("방 [{}]의 멤버 [{}] 나감", roomId, memberNum);
		   }
		   
		   responseData.put("roomId", roomId);
		   responseData.put("memberNum", memberNum);
	       responseData.put("initStatus", newStatus);
	       
		   return ResponseEntity.status(HttpStatus.OK).body(responseData);
	   }catch(Exception e)
	   {
		   responseData.put("message", e.getMessage());
		   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
	   }
	}
	
	
	public ResponseEntity<Map<String,Object>> deleteRoom(Long roomId)
	{
		Map<String, Object> responseData = new HashMap<>();
		
		try {
			String roomKey = "room:"+roomId;
			
			if(Boolean.FALSE.equals(redisTemplate.hasKey(roomKey))) {
				responseData.put("message", "해당 방이 존재하지 않습니다.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
			}
			
			redisTemplate.delete(roomKey);
			log.info("[{}]번 방 삭제 완료", roomId);
			responseData.put("message", roomId+"번 방 삭제 완료");
			return ResponseEntity.status(HttpStatus.OK).body(responseData);
			
		}catch(Exception e)
		{
			responseData.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
		}
		
	}
}
