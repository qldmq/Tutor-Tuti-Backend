package com.example.tutoring.jwt;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.tutoring.dto.RefreshTokenDto;
import com.example.tutoring.entity.RefreshToken;
import com.example.tutoring.repository.RefreshTokenRespository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private final Key key;
	private static final long accessTokenValidity = 1000L * 60 * 30; // 30분
	private static final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // 7일
    //private final Map<String, String> refreshTokenStore = new ConcurrentHashMap<>();

    @Autowired
    private RefreshTokenRespository refreshTokenRespository;
        
	
	public JwtTokenProvider(@Value("${jwt.secret}")String secretKey)
	{
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String createAccessToken(String memberNum) {
        return createToken(memberNum, accessTokenValidity);
    }

    public String createRefreshToken(String memberNum) {
        String refreshToken = createToken(memberNum, refreshTokenValidity);
        Instant expiryDate = Instant.now().plusSeconds(60 * 60 * 24 * 7); // 7일 후 만료

        RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder()
                .memberNum(Integer.parseInt(memberNum))
                .reToken(refreshToken)
                .expiryDate(expiryDate)
                .build();
        
        refreshTokenRespository.save(RefreshToken.toEntity(refreshTokenDto));
                
        return refreshToken;
    }
		
	private String createToken(String memberNum, long validity)
	{
		Claims claims = Jwts.claims().setSubject(memberNum);
		Date now = new Date();
		Date expiration = new Date(now.getTime() + validity);
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(expiration)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public boolean isAccessTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // 만료됨
        } catch (Exception e) {
            return true; // 잘못된 토큰
        }
    }
	
	
	public boolean isRefreshTokenExpired(Integer memberNum) {
        Optional<RefreshToken> storedToken = refreshTokenRespository.findById(memberNum);
        return storedToken.map(token -> token.getExpiryDate().isBefore(Instant.now())).orElse(true);
    }
	
	public String reissueAccessToken(Integer memberNum) {

        if (isRefreshTokenExpired(memberNum)) {
            throw new RuntimeException("Refresh Token이 만료되었습니다.");
        }

        Optional<RefreshToken> storedToken = refreshTokenRespository.findById(memberNum);
        if (storedToken.isEmpty()) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }
       
        return createAccessToken(Integer.toString(memberNum));
    }
			

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public String getMemberNum(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody().getSubject();
    }
		
}
