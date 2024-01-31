package com.crescendo.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class snsLoginService {


    public void googleLogin(Map<String, String> requestParam, HttpSession session) {
        // 인가 코드를 가지고 토큰을 발급받는 요청보내기
        String googleAccessToken = getGoogleAccessToken(requestParam);
        System.out.println("kakaoAccessToken = " + googleAccessToken);
        JsonNode userResource = getUserResource(googleAccessToken);
        String id = userResource.get("id").asText();
        String email = userResource.get("email").asText();
        String nickname = userResource.get("name").asText();
        System.out.println("id = " + id);
        System.out.println("email = " + email);
        System.out.println("nickname = " + nickname);

    }

    private String getGoogleAccessToken(Map<String, String> requestParam) {

        // 요청 URI
        String requestUri = "https://oauth2.googleapis.com/token";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 바디에 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", requestParam.get("client_id"));
        params.add("client_secret", requestParam.get("client_secret"));
        params.add("redirect_uri", "http://localhost:8484/api/auth/oauth2/googleInfo");
        params.add("code", requestParam.get("code"));

        //구글 인증서버로 post요청날리기
        RestTemplate template= new RestTemplate();
        HttpEntity<Object> requestEntity = new HttpEntity<>(params, headers);
        /*
        -RestTemplate 객체가 REST API 통신을 위한 API인데 (자바스크립트 fetch 역할)
        - 서버에 통신을 보내면서 응답을 받을 수 있는 메서드가 exchange
        param1 요청 URL
        param2: 요청방식(get,post,put..)
        param3: 요청 헤더와 요청 바디 정보- HttpEntity로 포장해서 줘야함.
        param4:응답 결과를 어떤 타입으로 받아낼 것인디(ex:DTO로 받을 건지,,Map으로 받을 건지)

         */
        ResponseEntity<Map> responseEntity = template.exchange(requestUri, HttpMethod.POST, requestEntity, Map.class);
        //응답데이터에서 JSON 추출
        Map<String,Object> reponseJSON = (Map<String, Object>) responseEntity.getBody();
        log.info("응답데이터 :{}",reponseJSON);

        //access token 추출
        String accessToken = (String)reponseJSON.get("access_token");

        return accessToken;


    }

    private JsonNode getUserResource(String accessToken) {

        RestTemplate restTemplate = new RestTemplate();
        String resourceUri="https://www.googleapis.com/oauth2/v2/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
}
