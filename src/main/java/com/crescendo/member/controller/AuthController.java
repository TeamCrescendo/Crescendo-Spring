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

import javax.servlet.http.HttpSession;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class AuthController {
    private final MemberService memberService;

    // 회원가입(계정명, 비밀번호, 이메일, 이름)
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

    // 유저 찾기
    @GetMapping("/find/{account}")
    public ResponseEntity<?> findUser(@PathVariable String account){
        if (account == null || account.isBlank()){
            return ResponseEntity.badRequest().body("계정명을 정확히 적어주세요");
        }
        try{
            Member foundUser = memberService.findUser(account);
            return ResponseEntity.ok().body(new FindUserResponseDTO(foundUser));
        }catch (NoMatchAccountException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 비교
    @GetMapping("/compare")
    public ResponseEntity<?> compareTo(@AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        Member user = memberService.findUser(tokenUserInfo.getAccount());
        LoginUserResponseDTO loginUserResponseDTO = new LoginUserResponseDTO(user);
        return ResponseEntity.ok().body(loginUserResponseDTO);
    }



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
