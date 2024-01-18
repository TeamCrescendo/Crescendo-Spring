package com.crescendo.service;

import com.crescendo.dto.request.SignInRequestDTO;
import com.crescendo.dto.request.SignUpRequestDTO;
import com.crescendo.entity.Member;
import com.crescendo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;


    // 회원 가입 처리
    public void signUp(SignUpRequestDTO dto){
        memberRepository.save(dto.toEntity(encoder));
    }

    // 로그인 처리
    public String signIn(SignInRequestDTO dto){
        Member foundMember = memberRepository.getOne(dto.getAccount());
        if(foundMember == null){
            return "일치하는 계정이 없습니다.";
        }
        String encodedPassword = foundMember.getPassword();
        String rawPassword = dto.getPassword();
        if(!encoder.matches(rawPassword, encodedPassword)){
            return "비밀번호가 틀렸습니다.";
        }
        return "로그인 성공 했습니다.";
    }



}
