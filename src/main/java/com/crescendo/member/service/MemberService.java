package com.crescendo.member.service;

import com.crescendo.dto.request.ModifyMemberRequestDTO;
import com.crescendo.member.dto.request.SignInRequestDTO;
import com.crescendo.member.dto.request.SignUpRequestDTO;
import com.crescendo.member.entity.Member;
import com.crescendo.member.exception.*;
import com.crescendo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;


    // 회원 가입 처리
    public boolean signUp(SignUpRequestDTO dto){
        if(dto==null){
            log.warn("회원정보가 없습니다");
            throw new NoRegisteredArgumentsException("회원가입 입력정보가 없습니다!");
        }
        String account = dto.getAccount();
        if(memberRepository.existsById(account)){
            log.warn("계정이 중복되었습니다!! -{}.", account);
            throw new DuplicatedAccountException("중복된 계정입니다!!");
        }
        Member save = memberRepository.save(dto.toEntity(encoder));
        log.info("회원가입 성공!! saved user - {}", save);
        return true;
    }

    // 로그인 처리
    public Member signIn(SignInRequestDTO dto){
        if(dto==null){
            log.warn("로그인 정보가 없습니다.");
            throw new NoLoginArgumentsException("로그인 정보가 없습니다");
        }
        Member foundMember = memberRepository.getOne(dto.getAccount());
        if(foundMember == null){
            log.warn("일치하는 계정이 없습니다.");
            throw new NoMatchAccountException("일치하는 계정이 없습니다");
        }
        String encodedPassword = foundMember.getPassword();
        String rawPassword = dto.getPassword();
        if(!encoder.matches(rawPassword, encodedPassword)){
            log.warn("비밀번호가 틀렸습니다.");
            throw new IncorrectPasswordException("비밀번호가 틀렸습니다.");
        }
        return foundMember;
    }

    public Member findUser(String account){
        return memberRepository.getOne(account);
    }

    public boolean modifyUser(ModifyMemberRequestDTO dto){
        System.out.println("dto = " + dto);
        Member foundMember = memberRepository.getOne(dto.getAccount());
        System.out.println("foundMember = " + foundMember);
        if (foundMember == null){
            return false;
        }
        foundMember.setPassword(encoder.encode(dto.getPassword()));
        foundMember.setEmail(dto.getEmail());
        foundMember.setUserName(dto.getUserName());
        return true;
    }

}
