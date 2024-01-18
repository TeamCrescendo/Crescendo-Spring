package com.crescendo.dto.response;

import com.crescendo.entity.Member;
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

    public LoginUserResponseDTO(Member member) {
        this.account=member.getAccount();
        this.userName=member.getUserName();
        this.email=member.getEmail();
        this.profileImageUrl=member.getProfileImageUrl();
        this.regDateTime=member.getRegDateTime();
        this.auth=member.getAuth();
        this.userDownloadChance=member.getUserDownloadChance();
    }
}
