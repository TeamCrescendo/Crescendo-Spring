package com.crescendo.controller;

import com.crescendo.dto.request.ModifyMemberRequestDTO;
import com.crescendo.dto.request.SignInRequestDTO;
import com.crescendo.dto.request.SignUpRequestDTO;
import com.crescendo.dto.response.FindUserPackResponseDTO;
import com.crescendo.dto.response.FindUserResponseDTO;
import com.crescendo.dto.response.LoginResultResponseDTO;
import com.crescendo.dto.response.LoginUserResponseDTO;
import com.crescendo.entity.Member;
import com.crescendo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.UUID;

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
    public ResponseEntity<?> signUp(@Validated @RequestBody SignUpRequestDTO dto){
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
        session.setMaxInactiveInterval(60*60);

        return ResponseEntity.ok().body(LoginResultResponseDTO.builder().result(true).build());
    }

    // 유저 정보 주기
    @GetMapping("/find/{account}")
    public ResponseEntity<?> findUser(@PathVariable String account){
        Member foundUser = memberService.findUser(account);
        if(foundUser != null){
            FindUserResponseDTO foundUserDTO = new FindUserResponseDTO(foundUser);
            return ResponseEntity.ok().body(FindUserPackResponseDTO.builder().findUser(foundUserDTO).build());
        }
        return ResponseEntity.badRequest().body("그런 회원 없어");
    }

    @GetMapping("/compare")
    public ResponseEntity<?> compareTo(HttpSession session){
        LoginUserResponseDTO attribute = (LoginUserResponseDTO) session.getAttribute("login");
        return ResponseEntity.ok().body(attribute);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        // 세션 무효화
        session.invalidate();
        return ResponseEntity.ok().body("Logout successful");
    }


    // 회원 정보 수정
    @RequestMapping(method = {PUT, PATCH}, path = "/modify")
    public ResponseEntity<?> updateUser(@RequestBody ModifyMemberRequestDTO dto){
        log.info("Modify!!");
        try{
            boolean flag = memberService.modifyUser(dto);
            return ResponseEntity.ok().body(flag);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
