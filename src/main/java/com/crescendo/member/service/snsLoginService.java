package com.crescendo.member.service;

import com.crescendo.Util.FileDownloader;
import com.crescendo.member.dto.request.GoogleSignInRequestDTO;
import com.crescendo.member.dto.request.GoogleSignUpRequestDTO;
import com.crescendo.member.dto.request.SignInRequestDTO;
import com.crescendo.member.dto.request.SignUpRequestDTO;
import com.crescendo.member.dto.response.LoginUserResponseDTO;
import com.crescendo.member.entity.Member;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.member.util.FileUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${file.upload.root-path}")
    String imgSavePath;

    @Value("${spring.security.oauth2.google.client-id}")
    String clientId;
    @Value("${spring.security.oauth2.google.redirect-uri}")
    String redirectUri;
    @Value("${spring.security.oauth2.google.client-secret}")
    String clientSecret;

    @Value("${spring.security.oauth2.google.resource-uri}")
    String resourceUri;

    public LoginUserResponseDTO googleLoginByaccess(String accessToken) throws IOException {

        //토큰을 가지고 id, email,nickname 정보를 가져오는 요청 보내기
        JsonNode userResource = getUserResource(accessToken);

        //유저 정보 파싱

        String id = userResource.get("id").asText();
        String email = userResource.get("email").asText();
        String nickname = userResource.get("name").asText();
        String pictureUri = userResource.get("picture").asText();

        Map<String, String> params = new HashMap<>();
        params.put("id",id);
        params.put("email",email);
        params.put("nickname",nickname);
        params.put("pictureUri",pictureUri);

        return saveOrUpdate(params);
    }


    //새로운 회원이면 저장하고-> 로그인
    //기존 회원이면 그냥 로그인

    private LoginUserResponseDTO saveOrUpdate(Map<String, String> params) throws IOException {
        //email을 꺼내 기존 회원인지 조회
        String email = params.get("email");
        String account = params.get("id");
        String nickname = params.get("nickname");
        String imgUri=params.get("pictureUri");

        boolean isExist = memberRepository.existsByEmail(email);

        if(isExist){ //계정이 존재하면
            //멤버를 꺼내오기
            return getLoginUserResponseDTO(email);


        }else{//계정이 존재하지 않으면
            log.info("계정이존재하지 않나요");
            //랜덤 패스워드 만들기
            String password= UUID.randomUUID().toString();
            //이미지 소스에서 파일 다운 가져오기

            GoogleSignUpRequestDTO dto = GoogleSignUpRequestDTO.builder()
                    .account(account)
                    .userName(nickname)
                    .email(email)
                    .password(password)
                    .profileImage(imgUri).build();

            //회원가입 진행
            memberService.signUp(dto);

            //로그인 바로 되게 하기
            return getLoginUserResponseDTO(email);


        }


    }

    private LoginUserResponseDTO getLoginUserResponseDTO(String email) {
        Member member = memberRepository.findMemberByEmail(email);

        //
        GoogleSignInRequestDTO loginDTO = GoogleSignInRequestDTO.builder()
                .account(member.getAccount())
                .autoLogin(true).build();

        LoginUserResponseDTO loginUserResponseDTO = memberService.signIn(loginDTO);
        return loginUserResponseDTO;
    }


    private JsonNode getUserResource(String accessToken) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }



}
