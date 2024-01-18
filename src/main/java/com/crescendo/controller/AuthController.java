package com.crescendo.controller;

import com.crescendo.dto.request.SignInRequestDTO;
import com.crescendo.dto.request.SignUpRequestDTO;
import com.crescendo.dto.response.LoginResultResponseDTO;
import com.crescendo.dto.response.LoginUserResponseDTO;
import com.crescendo.entity.Member;
import com.crescendo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000"})
public class AuthController {
    private final MemberService memberService;

    // 회원가입(계정명, 비밀번호, 이메일, 이름)
    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDTO dto){
        System.out.println("dto = " + dto);
        memberService.signUp(dto);
        return ResponseEntity.ok().body(true);
    }

    // 로그인(계정명, 비밀번호, 자동로그인 여부)
    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody SignInRequestDTO dto, HttpSession session){
        String result = memberService.signIn(dto);
        if(result.equals("일치하는 계정이 없습니다.")){
            return ResponseEntity.ok().body(LoginResultResponseDTO.builder().result(false).build());
        }else if(result.equals("비밀번호가 틀렸습니다.")){
            return ResponseEntity.ok().body(LoginResultResponseDTO.builder().result(false).build());
        }

        Member foundUser = memberService.findUser(dto.getAccount());
        LoginUserResponseDTO login = new LoginUserResponseDTO(foundUser);
        System.out.println("login = " + login);
        session.setAttribute("login", login);

        return ResponseEntity.ok().body(LoginResultResponseDTO.builder().result(true).dto(login).build());
    }

    // 로그인 성공하면


}
