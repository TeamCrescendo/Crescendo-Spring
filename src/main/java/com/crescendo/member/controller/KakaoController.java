package com.crescendo.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/kauth")
@CrossOrigin(origins = {"http://localhost:3000"})
public class KakaoController {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoAppKey;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    //카카오 인가코드 발급 요청 ~
    @GetMapping("/kakao/login")
    public String KakaoLogin(){
        String uri = "";

        return "redirect:" + uri;
    }
}
