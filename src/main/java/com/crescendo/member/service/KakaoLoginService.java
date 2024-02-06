package com.crescendo.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class KakaoLoginService {

    //카카오 로그인 처리
    public void kakaoLogin(){

        //인가 코드를 가지고 토큰을 발급 받는 요청

    }

    //토큰 발급 요청
    private String getKakaoAccessToken(){

        //요청 URI
        String requestUri = "";

        return null;
    }
}
