package com.crescendo.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member")
public class Member {
    @Id
    @Column(name = "account")
    private String account; // 유저 아이디

    @Column(name = "password",nullable = false, length = 30)
    private String password; // 유저 패스워드

    @Column(name = "user_name", nullable = false)
    private String userName; // 유저 닉네임

    @Column(name = "email",unique = true, nullable = false)
    private String email; // 유저 이메일

    @Column(name = "profile_image_url")
    private String profileImageUrl; // 유저 프로필 이미지

    @CreationTimestamp
    @Column(name = "reg_date_time",updatable = false)
    private LocalDateTime regDateTime; // 유저 가입 시간

    @Enumerated(EnumType.STRING)
    @Builder.Default()
    private Auth auth = Auth.USER;// 유저 권한

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.NORMAL; // 유저 상태 (정상, 임시, 벤)

    @Column(name = "auto_login")
    @Builder.Default
    private boolean autoLogin = false; // 유저 오토 로그인 여부

    @Column(name = "user_download_chance")
    @Builder.Default
    private int userDownloadChance = 5;

    public enum Auth{
        ADMIN, USER
    }
    public enum Status{
        NORMAL, TEMPORARY, BAN
    }
}


