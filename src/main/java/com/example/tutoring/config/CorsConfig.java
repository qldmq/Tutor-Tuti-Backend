package com.example.tutoring.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer{

	@Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 요청에 대해 CORS 허용
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000","http://localhost:8888")  // 특정 Origin 허용 , SSE알림 테스트 페이지
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH")
                .allowedHeaders("Authorization", "*")
                .allowCredentials(true);  // 자격 증명 허용
    }
	
}
