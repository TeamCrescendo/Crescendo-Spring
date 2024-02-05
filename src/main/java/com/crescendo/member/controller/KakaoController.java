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
@RequestMapping("/api/auth/oauth2")
@CrossOrigin(origins = {"http://localhost:3000"})
public class KakaoController {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoAppKey;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    //카카오 인가코드 발급 요청 ~
    @GetMapping("/kakao/login")
    public String KakaoLogin(){
        String uri = "https://kauth.kakao.com/oauth/authorize";
        uri += "?client_id=" + kakaoAppKey;
        uri += "&redirect_uri=" + kakaoRedirectUri;
        uri += "&response_type=code";

        return "redirect:" + uri;
    }

    //인가 코드 받기
    @GetMapping("/auth/kakao")
    public String snsKakao(String code){
        log.info("카카오 로그인 인가 코드 : {}",code);

        // 인가코드를 가지고 카카오 인증서버에 토큰 발급 요청을 보내야 함
        // server to server 통신


        return "redirect:/api/auth/login";
    }
}
