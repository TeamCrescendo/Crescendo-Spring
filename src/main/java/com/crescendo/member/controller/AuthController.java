package com.crescendo.member.controller;

import com.crescendo.dto.request.ModifyMemberRequestDTO;
import com.crescendo.member.dto.request.SignInRequestDTO;
import com.crescendo.member.dto.request.SignUpRequestDTO;
import com.crescendo.member.dto.response.FindUserPackResponseDTO;
import com.crescendo.member.dto.response.FindUserResponseDTO;
import com.crescendo.member.dto.response.LoginResultResponseDTO;
import com.crescendo.member.dto.response.LoginUserResponseDTO;
import com.crescendo.member.entity.Member;
import com.crescendo.member.exception.DuplicatedAccountException;
import com.crescendo.member.exception.NoRegisteredArgumentsException;
import com.crescendo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> signUp(@Validated @RequestBody SignUpRequestDTO dto, BindingResult result){
        if(result.hasErrors()){
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        try {
            boolean flag = memberService.signUp(dto);
            return ResponseEntity.ok().body(true);
        }catch (DuplicatedAccountException e){
            log.warn("이메일이 중복되었습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (NoRegisteredArgumentsException e){
            log.warn("계정정보가 안왔습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        }

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
