package com.crescendo.member.controller;

import com.crescendo.member.dto.request.DuplicateCheckDTO;
import com.crescendo.member.dto.request.ModifyMemberRequestDTO;
import com.crescendo.member.dto.request.SignInRequestDTO;
import com.crescendo.member.dto.request.SignUpRequestDTO;
import com.crescendo.member.dto.response.FindUserResponseDTO;
import com.crescendo.member.dto.response.LoginResultResponseDTO;
import com.crescendo.member.dto.response.LoginUserResponseDTO;
import com.crescendo.member.entity.Member;
import com.crescendo.member.exception.*;
import com.crescendo.member.service.MemberService;
import com.crescendo.member.util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class AuthController {
    private final MemberService memberService;

    // 일반회원가입(계정명, 비밀번호, 이메일, 이름)
    @PostMapping("/register")
    public ResponseEntity<?> signUp(@Validated SignUpRequestDTO dto, BindingResult result){
        if(result.hasErrors()){
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        try {
            boolean flag = memberService.signUp(dto);
            return ResponseEntity.ok().body(flag);
        }catch (DuplicatedAccountException e){
            log.warn("계정이 중복되었습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (NoRegisteredArgumentsException e){
            log.warn("계정정보가 안왔습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (DuplicateUserNameException | DuplicateEmailException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //구글 회원가입(계정명, 비밀번호, 이메일 ,이름)
    @GetMapping("/register/google")
    public void googleSignUp(HttpServletResponse response) throws IOException {

        String googleBasicUrl = "https://accounts.google.com/o/oauth2/v2/auth";
//        https://accounts.google.com/o/oauth2/v2/auth?
//        scope=https%3A//www.googleapis.com/auth/drive.metadata.readonly&
//                include_granted_scopes=true&
//                response_type=token&
//                state=state_parameter_passthrough_value&
//                redirect_uri=https%3A//oauth2.example.com/code&
//                client_id=client_id
//        //
//        response.sendRedirect(url);
//
//        memberService.googleLogin(code,registrationId);
        // 구글에 접속해서 회원가입 진행
    }
    



    // 로그인(계정명, 비밀번호, 자동로그인 여부)
    @PostMapping("/login")
    public ResponseEntity<?> signIn(@Validated @RequestBody SignInRequestDTO dto, HttpSession session, BindingResult result){
        if(result.hasErrors()){
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try{
            LoginUserResponseDTO loginUserResponseDTO = memberService.signIn(dto);

            session.setAttribute("login", loginUserResponseDTO);
            session.setMaxInactiveInterval(60*60);

            return ResponseEntity.ok().body(loginUserResponseDTO);
        }catch (NoLoginArgumentsException | NoMatchAccountException | IncorrectPasswordException e){
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
    public ResponseEntity<?> isDuplicate(@Validated @RequestBody DuplicateCheckDTO dto, BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().body(result.toString());
        }
        try{
            boolean flag = memberService.duplicateCheck(dto);
            return ResponseEntity.ok().body(flag);
        }catch (NoDuplicateCheckArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
