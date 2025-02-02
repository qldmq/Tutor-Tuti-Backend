package com.example.tutoring.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.tutoring.dto.RefreshTokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "refreshToken")
public class RefreshToken {
	 
    @Id
    @Column(name = "memberNum", nullable = false)
    private Integer memberNum; 

    @Column(nullable = false, unique = true)
    private String reToken;

    @Column(nullable = false)
    private Instant expiryDate;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberNum", referencedColumnName = "memberNum", insertable = false, updatable = false)  // `memberNum`을 외래 키로 매핑
    private Member member;

    public boolean isExpired() {
        return expiryDate.isBefore(Instant.now());
    }

    public static RefreshToken toEntity(RefreshTokenDto dto) {
        return RefreshToken.builder()
        		.memberNum(dto.getMemberNum())
                .reToken(dto.getReToken())
                .expiryDate(dto.getExpiryDate())
                .build();
    }
}
