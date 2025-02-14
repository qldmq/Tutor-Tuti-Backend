package com.example.tutoring.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class UploadService {
	
	@Autowired
	S3Uploader s3Uploader;
	
	
	public Map<String,Object> uploadProfileImg(MultipartFile image)
	{
		Map<String,Object> responseMap = new HashMap<String, Object>();
		
		try {
            // 로컬에 임시 파일로 저장
            File tempFile = File.createTempFile("upload-", image.getOriginalFilename());
            image.transferTo(tempFile);

            // S3에 업로드
            String s3Url = s3Uploader.upload(tempFile.getAbsolutePath());

            // 로컬 임시 파일 삭제
            if (tempFile.exists() && tempFile.delete()) {
                log.info("Temporary file deleted: " + tempFile.getName());
            }

            responseMap.put("status", 200);
            responseMap.put("url",s3Url);
            
            return responseMap;
            
            // 업로드된 파일의 URL 반환
        } catch (IOException e) {
            log.error("File upload failed: " + e.getMessage(), e);           

            responseMap.put("message", e.getMessage());            
            return responseMap;
        }
				
	}
}
