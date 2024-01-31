//package com.crescendo.googleT;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping(value = "/api/auth/oauth2/google", produces = "application/json")
//public class snsLoginController {
//    LoginService  loginService;
//
//    public snsLoginController(LoginService loginService) {
//        this.loginService = loginService;
//    }
//
//    @GetMapping("/oauth2/google/code/{registrationId}")
//    public void googleLogin(@RequestParam String code, @PathVariable String registrationId) {
//        loginService.socialLogin(code, registrationId);
//    }
//}
















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


//
//private final LoginService loginService;
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
