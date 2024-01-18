package com.crescendo.controller;

import com.crescendo.dto.request.SignInRequestDTO;
import com.crescendo.dto.request.SignUpRequestDTO;
import com.crescendo.entity.Member;
import com.crescendo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/auth")
public class AuthController {
    private final MemberService memberService;

    // 회원가입(계정명, 비밀번호, 이메일, 이름)
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDTO dto){
        System.out.println("dto = " + dto);
        memberService.signUp(dto);
        return ResponseEntity.ok().body("ok");
    }

    // 로그인(계정명, 비밀번호, 자동로그인 여부)
    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody SignInRequestDTO dto){
        String result = memberService.signIn(dto);
        if(result.equals("일치하는 계정이 없습니다.")){
            ResponseEntity.ok().body(result);
        }else if(result.equals("비밀번호가 틀렸습니다.")){
            ResponseEntity.ok().body(result);
        }
        return ResponseEntity.ok().body(result);
    }




}
