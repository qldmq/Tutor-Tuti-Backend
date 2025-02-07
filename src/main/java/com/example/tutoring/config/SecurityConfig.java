package com.example.tutoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.tutoring.jwt.JwtAuthenticationFilter;
import com.example.tutoring.oauth2.CustomOAuth2UserService;
import com.example.tutoring.oauth2.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;	
	private final CustomOAuth2UserService customOAuth2UserService;	
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
             
    	 http
         .csrf().disable()
         .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
         .and()
         .authorizeHttpRequests()
         .antMatchers("/member/**","/oauth/**","/image/**").permitAll()
         //.antMatchers("/member/**").hasRole("USER")
         .anyRequest().authenticated()
         .and()
         .oauth2Login()
         .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
         .successHandler(oAuth2LoginSuccessHandler)
         .and()
         .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
     
     return http.build();
    	
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화
    }
}
