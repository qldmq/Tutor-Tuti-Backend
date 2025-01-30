package com.example.tutoring.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.example.tutoring.dto.MemberDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberNum")
    private Integer memberNum;

    @Column(name = "memberId")
    private String memberId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "loginType")
    private Integer loginType;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profileImg")
    private String profileImg;

    @Column(name = "introduction")
    private String introduction;
    
    public static Member toEntity(MemberDto dto) {
        return Member.builder()
                .memberId(dto.getMemberId())
                .memberNum(dto.getMemberNum())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .loginType(dto.getLoginType())
                .nickname(dto.getNickname())
                .profileImg(dto.getProfileImg())
                .introduction(dto.getIntroduction())
                .build();
    }
}
