package com.crescendo.controller;

import com.crescendo.dto.request.SignInRequestDTO;
import com.crescendo.dto.request.SignUpRequestDTO;
import com.crescendo.entity.Member;
import com.crescendo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDTO dto){
        System.out.println("dto = " + dto);
        memberService.signUp(dto);
        return ResponseEntity.ok().body("ok");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody SignInRequestDTO dto){
        //memberService.authenti
        return null;
    }



}
