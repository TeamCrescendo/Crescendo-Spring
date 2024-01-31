package com.crescendo.member.controller;

import com.crescendo.member.dto.request.DuplicateCheckDTO;
import com.crescendo.member.dto.request.SignInRequestDTO;
import com.crescendo.member.dto.request.SignUpRequestDTO;
import com.crescendo.member.dto.response.LoginUserResponseDTO;
import com.crescendo.member.exception.*;
import com.crescendo.member.service.MemberService;
//import com.crescendo.member.service.snsLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class AuthController {
    private final MemberService memberService;
//    private final snsLoginService snsLoginService;
//
//    @Value("${spring.security.oauth2.client.registration.google.client-id}")
//    private String clientId;
//    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
//    private String redirectUri;
//    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
//    private String clientSecret;
//    @Value("${spring.security.oauth2.client.registration.google.scope}")
//    private String scope;

    // 일반회원가입(계정명, 비밀번호, 이메일, 이름)
    @PostMapping("/register")
    public ResponseEntity<?> signUp(@Validated SignUpRequestDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        try {
            boolean flag = memberService.signUp(dto);
            return ResponseEntity.ok().body(flag);
        } catch (DuplicatedAccountException e) {
            log.warn("계정이 중복되었습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoRegisteredArgumentsException e) {
            log.warn("계정정보가 안왔습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DuplicateUserNameException | DuplicateEmailException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //구글 인가코드 발급 요청
//    @GetMapping("/google/login")
//    public void googleSignUp(HttpServletResponse response) throws IOException {
//        //1. 구글 접속해서 정보가져오기
//        //2.email이 db에 없으면 회원가입진행
//        //3. email이 db에 있으면 로그인 진행
//
//        /*
//        http://localhost:8484/api/
//        auth#access_token=ya29.a0AfB_byBNvCeLvxsc7kP37hoh33UD8eIf6BRLGnLRk7EdIzVDse87nLbf4ORnLM6trj0sogecluhDNbqflrzgSuJOC2WdLv1wHMKIb40RHjUquHssCVzpINwuKYFuWyAbPvm0WxB9N6x2IZmSom-aBXO22R-u7p-iRGlPA44SjwaCgYKAckSARASFQHGX2MiV4sIuPB58Hxr7TbuVIF7Ww0177
//        &token_type=Bearer
//        &expires_in=3599
//        &scope=email%20https://www.googleapis.com/auth/userinfo.email%20openid&authuser=0&prompt=none
//         */
//
//        /*
//        https://accounts.google.com/o/oauth2/v2/auth
//        ?redirect_uri=https%3A%2F%2Fdevelopers.google.com%2Foauthplayground
//        &prompt=consent&response_type=code
//        &client_id=407408718192.apps.googleusercontent.com
//        &scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile
//        &access_type=offline
//         */
//
//        System.out.println("들어오나요?");
//        String uri = "https://accounts.google.com/o/oauth2/v2/auth";
//        uri += "?client_id=" + clientId;
//        uri += "&redirect_uri=" + "http://localhost:8484/oauth2callback";
//        uri += "&response_type=code";
//        uri += "&scope=https://www.googleapis.com/auth/userinfo.profile";
//        response.sendRedirect(uri);
////        memberService.googleLogin(code,registrationId);
//        // 구글에 접속해서 회원가입 진행
//    }
//
//    //구글 인가코드 받기
//    @GetMapping("/oauth2callback")
//    public String authGoogle(String code, HttpSession session) throws IOException {
//        //인가 코드를 가지고 구글 인증서버에 토큰 발급 요청을 보냄
//
//        Map<String, String> params = new HashMap<>();
//        params.put("client_id",clientId);
//        params.put("client_secret",clientSecret);
//        params.put("code",code);
//        params.put("grant_type","authorization_code");
//        params.put("redirect_uri","http://localhost:8484/api/auth/googleInfo");
//
//        snsLoginService.googleLogin(params,session);
//        return null;
//
//
//    }



    // 로그인(계정명, 비밀번호, 자동로그인 여부)
    @PostMapping("/login")
    public ResponseEntity<?> signIn(@Validated @RequestBody SignInRequestDTO dto, HttpSession session, BindingResult result) {
        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try {
            LoginUserResponseDTO loginUserResponseDTO = memberService.signIn(dto);

            session.setAttribute("login", loginUserResponseDTO);
            session.setMaxInactiveInterval(60 * 60);

            return ResponseEntity.ok().body(loginUserResponseDTO);
        } catch (NoLoginArgumentsException | NoMatchAccountException | IncorrectPasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }


//    // 유저 찾기
//    @GetMapping("/find/{account}")
//    public ResponseEntity<?> findUser(@PathVariable String account){
//        if (account == null || account.isBlank()){
//            return ResponseEntity.badRequest().body("계정명을 정확히 적어주세요");
//        }
//        try{
//            Member foundUser = memberService.findUser(account);
//            return ResponseEntity.ok().body(new FindUserResponseDTO(foundUser));
//        }catch (NoMatchAccountException e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }


    // 중복 체크 ( 이메일 , 유저 이름, 계정)
    @PostMapping("/duplicate")
    public ResponseEntity<?> isDuplicate(@Validated @RequestBody DuplicateCheckDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.toString());
        }
        try {
            boolean flag = memberService.duplicateCheck(dto);
            return ResponseEntity.ok().body(flag);
        } catch (NoDuplicateCheckArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
