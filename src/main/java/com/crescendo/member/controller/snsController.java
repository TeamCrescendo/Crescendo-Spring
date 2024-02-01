package com.crescendo.member.controller;

import com.crescendo.member.dto.response.LoginUserResponseDTO;
import com.crescendo.member.exception.IncorrectPasswordException;
import com.crescendo.member.exception.NoLoginArgumentsException;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.service.snsLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/oauth2")
@CrossOrigin(origins = {"http://localhost:3000"})
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


    @CrossOrigin(origins = {"http://localhost:3000","http://localhost:8484/api/auth/oauth2/googleInfo"})
    @GetMapping("/google")
    public void googleSignUp(HttpServletResponse response) throws IOException {
        //1. 구글 접속해서 정보가져오기
        //2.email이 db에 없으면 회원가입진행
        //3. email이 db에 있으면 로그인 진행
        String redirecrUri="http://localhost:3000";


        System.out.println("들어오나요?");
        String uri = "https://accounts.google.com/o/oauth2/v2/auth";
        uri += "?client_id=" + clientId;
        uri += "&redirect_uri=" + redirecrUri;
        uri += "&response_type=code";
        uri += "&scope=https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";

        String scope="https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";


        HttpHeaders headers = new HttpHeaders();

        // 요청 바디에 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("uri", "https://accounts.google.com/o/oauth2/v2/auth");
        params.add("redirect_uri", "http://localhost:3000");
        params.add("response_type", "code");
        params.add("scope", scope);
        RestTemplate template= new RestTemplate();
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        template.exchange("https://accounts.google.com/o/oauth2/v2/auth", HttpMethod.GET, requestEntity, Map.class);



    }
    //구글 인가코드 받기
//    @CrossOrigin(origins = {"http://localhost:8484/api/auth/oauth2/google"})
//    @GetMapping("/googleInfo")
//    public ResponseEntity<?> authGoogle(String code, HttpSession session) throws IOException {
//        //인가 코드를 가지고 구글 인증서버에 토큰 발급 요청을 보냄
//        System.out.println(code);
//
//        Map<String, String> params = new HashMap<>();
//        params.put("client_id",clientId);
//        params.put("client_secret",clientSecret);
//        params.put("code",code);
//        params.put("grant_type","authorization_code");
//        params.put("redirect_uri","http://localhost:8484/api/auth/oauth2/googleInfo");
//
//
//
//        try {
//            LoginUserResponseDTO loginUserResponseDTO = snsLoginService.googleLogin(params, session);
//            System.out.println("loginUserResponseDTO = " + loginUserResponseDTO);
//
//            session.setAttribute("login", loginUserResponseDTO);
//            session.setMaxInactiveInterval(60 * 60);
//
//            // 리다이렉트할 URI로 ResponseEntity를 생성하여 반환
//            return ResponseEntity.status(HttpStatus.FOUND)
//
//                    .body(loginUserResponseDTO);
//
//
////            return ResponseEntity.ok().body(loginUserResponseDTO);
//        } catch (NoLoginArgumentsException | NoMatchAccountException | IncorrectPasswordException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//
//
//    }
    @GetMapping("/googleInfo/info")
    public ResponseEntity<?> getUserInfo(){
        System.out.println("info에는 뭐가..?");
        return ResponseEntity.ok().body("구글로그인 값 가져오기");

    }
}
