package com.crescendo.member.controller;

import com.crescendo.member.dto.response.LoginUserResponseDTO;
import com.crescendo.member.exception.IncorrectPasswordException;
import com.crescendo.member.exception.NoLoginArgumentsException;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.service.snsLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/oauth2")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class snsController {
    private final snsLoginService snsLoginService;

    @Value("${spring.security.oauth2.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.google.client-secret}")
    private String clientSecret;
//    @Value("${spring.security.oauth2.google.scope}")
//    private String scope;


    @GetMapping("/google")
    public void googleSignUp(HttpServletResponse response) throws IOException {
        //1. 구글 접속해서 정보가져오기
        //2.email이 db에 없으면 회원가입진행
        //3. email이 db에 있으면 로그인 진행

        /*
        http://localhost:8484/api/
        auth#access_token=ya29.a0AfB_byBNvCeLvxsc7kP37hoh33UD8eIf6BRLGnLRk7EdIzVDse87nLbf4ORnLM6trj0sogecluhDNbqflrzgSuJOC2WdLv1wHMKIb40RHjUquHssCVzpINwuKYFuWyAbPvm0WxB9N6x2IZmSom-aBXO22R-u7p-iRGlPA44SjwaCgYKAckSARASFQHGX2MiV4sIuPB58Hxr7TbuVIF7Ww0177
        &token_type=Bearer
        &expires_in=3599
        &scope=email%20https://www.googleapis.com/auth/userinfo.email%20openid&authuser=0&prompt=none
         */

        /*
        https://accounts.google.com/o/oauth2/v2/auth
        ?redirect_uri=https%3A%2F%2Fdevelopers.google.com%2Foauthplayground
        &prompt=consent&response_type=code
        &client_id=407408718192.apps.googleusercontent.com
        &scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile
        &access_type=offline
         */

        System.out.println("들어오나요?");
        String uri = "https://accounts.google.com/o/oauth2/v2/auth";
        uri += "?client_id=" + clientId;
        uri += "&redirect_uri=" + "http://localhost:8484/api/auth/oauth2/googleInfo";
        uri += "&response_type=code";
        uri += "&scope=https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";
        response.sendRedirect(uri);
    }
    //구글 인가코드 받기
    @GetMapping("/googleInfo")
    public ResponseEntity<?> authGoogle(String code, HttpSession session) throws IOException {
        //인가 코드를 가지고 구글 인증서버에 토큰 발급 요청을 보냄
        System.out.println(code);

        Map<String, String> params = new HashMap<>();
        params.put("client_id",clientId);
        params.put("client_secret",clientSecret);
        params.put("code",code);
        params.put("grant_type","authorization_code");
        params.put("redirect_uri","http://localhost:8484/api/auth/oauth2/googleInfo/info");



        try {
            LoginUserResponseDTO loginUserResponseDTO = snsLoginService.googleLogin(params, session);

            session.setAttribute("login", loginUserResponseDTO);
            session.setMaxInactiveInterval(60 * 60);

            return ResponseEntity.ok().body(loginUserResponseDTO);
        } catch (NoLoginArgumentsException | NoMatchAccountException | IncorrectPasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
}
