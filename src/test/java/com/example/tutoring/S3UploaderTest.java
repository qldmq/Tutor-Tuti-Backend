package com.example.tutoring;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.tutoring.service.S3Uploader;

@SpringBootTest
public class S3UploaderTest {
	
	private final Logger log = LoggerFactory.getLogger(S3UploaderTest.class);
	
	@Autowired
	private S3Uploader s3Uploader;
	
	@Test
	public void testUpload() {
		
		try {
			
			String filePath = "D:\\myproject\\tutoring\\img\\test.jpeg";
			String uploadName = s3Uploader.upload(filePath);
			log.info("upload Name : "+uploadName);
			
		} catch(Exception e)
		{
			log.info("Error Message : "+e.getMessage());
		}
	}
	
	@Test
	public void testRemove() {
		try {
			s3Uploader.removeS3File("test.jpeg");
		} catch(Exception e) {
			log.info("Error Message : "+e.getMessage());
		}
	}

}
