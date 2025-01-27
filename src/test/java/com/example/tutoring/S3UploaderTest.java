package com.example.tutoring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.tutoring.service.S3Uploader;

@SpringBootTest
public class S3UploaderTest {

	@Autowired
	private S3Uploader s3Uploader;
	
	@Test
	public void testUpload() {
		
		try {
			
			String filePath = "D:\\myproject\\tutoring\\img\\test.jpeg";
			String uploadName = s3Uploader.upload(filePath);
			System.out.println("upload Name : "+uploadName);
			
		} catch(Exception e)
		{
			System.out.println("Error Message : "+e.getMessage());
		}
	}
	
	@Test
	public void testRemove() {
		try {
			s3Uploader.removeS3File("test.jpeg");
		} catch(Exception e) {
			System.out.println("Error Message : "+e.getMessage());
		}
	}

}
