package com.crescendo.member.service;

import com.crescendo.member.dto.request.GoogleSignUpRequestDTO;
import com.crescendo.member.dto.request.SignInRequestDTO;
import com.crescendo.member.dto.request.SignUpRequestDTO;
import com.crescendo.member.entity.Member;
import com.crescendo.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class snsLoginService {
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    public void googleLogin(Map<String, String> requestParam, HttpSession session) throws IOException {
        // 인가 코드를 가지고 토큰을 발급받는 요청보내기
        String googleAccessToken = getGoogleAccessToken(requestParam);

        //토큰을 가지고 id, email,nickname 정보를 가져오는 요청 보내기
        JsonNode userResource = getUserResource(googleAccessToken);
        log.info("유저정보{}",userResource);
        String id = userResource.get("id").asText();
        String email = userResource.get("email").asText();
        String nickname = userResource.get("name").asText();
        String pictureUri = userResource.get("picture").asText();
        
        Map<String, String> params = new HashMap<>();
        params.put("id",id);
        params.put("email",email);
        params.put("nickname",nickname);
        params.put("pictureUri",pictureUri);

        saveOrUpdate(params);

    }

    //새로운 회원이면 저장하고-> 로그인
    //기존 회원이면 그냥 로그인

    private void saveOrUpdate(Map<String, String> params) throws IOException {
        //email을 꺼내 기존 회원인지 조회
        String email = params.get("email");
        String account = params.get("id");
        String nickname = params.get("nickname");
        String imgUri=params.get("pictureUri");

        boolean isExist = memberRepository.existsByEmail(email);

        if(isExist){ //계정이 존재하면
            //멤버를 꺼내오기
            Member member = memberRepository.findMemberByEmail(email);

            //
            SignInRequestDTO loginDTO = SignInRequestDTO.builder()
                    .account(member.getAccount())
                    .password(member.getPassword())
                    .autoLogin(true).build();

            memberService.signIn(loginDTO);


        }else{//계정이 존재하지 않으면
            log.info("계정이존재하지 않나요");
            //랜덤 패스워드 만들기
            String password= UUID.randomUUID().toString();
            //이미지 uri를 멀티파트 파일로 변환


            GoogleSignUpRequestDTO dto = GoogleSignUpRequestDTO.builder()
                    .account(account)
                    .userName(nickname)
                    .email(email)
                    .password(password)
                    .profileImagePath(imgUri).build();

            //회원가입 진행
            memberService.signUp(dto);
        }


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
