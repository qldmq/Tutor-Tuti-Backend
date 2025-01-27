package com.example.tutoring.service;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor

public class S3Uploader {

	private final Logger log = LoggerFactory.getLogger(S3Uploader.class);

	private final AmazonS3 amazonS3Client;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String upload(String filePath) throws RuntimeException {
		File targetFile = new File(filePath);
		String uploadImageUrl = putS3(targetFile, targetFile.getName());
		removeOriginalFile(targetFile);
		return uploadImageUrl;
	}
	
	private String putS3(File uploadFile, String fileName) throws RuntimeException{
		amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		
		return amazonS3Client.getUrl(bucket, fileName).toString();
	}
	
	private void removeOriginalFile(File targetFile) {
		if(targetFile.exists() && targetFile.delete())
		{
			log.info("File delete suceess");
			return;
		}
		
		log.info("fail to remove.");
	}
	
	public void removeS3File(String fileName) {
		final DeleteObjectRequest deleteObjectRequest 
		= new DeleteObjectRequest(bucket, fileName);
		
		amazonS3Client.deleteObject(deleteObjectRequest);
	}
	
}
