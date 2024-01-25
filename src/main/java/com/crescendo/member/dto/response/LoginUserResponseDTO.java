package com.crescendo.member.dto.response;

import com.crescendo.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUserResponseDTO {
    private String account;
    private String userName;
    private String email; // 유저 이메일
    private String profileImageUrl; // 유저 프로필 이미지
    private LocalDateTime regDateTime; // 유저 가입 시간
    private Member.Auth auth;// 유저 권한
    private Member.Status status;
    private Integer userDownloadChance;
    private String token;

    public LoginUserResponseDTO(Member member) {
        this.account=member.getAccount();
        this.userName=member.getUserName();
        this.email=member.getEmail();
        this.profileImageUrl=member.getProfileImageUrl();
        this.regDateTime=member.getRegDateTime();
        this.auth=member.getAuth();
        this.status = member.getStatus();
        this.userDownloadChance=member.getUserDownloadChance();
    }

    public LoginUserResponseDTO(Member member, String token) {
        this.account=member.getAccount();
        this.userName=member.getUserName();
        this.email=member.getEmail();
        this.profileImageUrl=member.getProfileImageUrl();
        this.regDateTime=member.getRegDateTime();
        this.auth=member.getAuth();
        this.status = member.getStatus();
        this.userDownloadChance=member.getUserDownloadChance();
        this.token = token;
    }
}
