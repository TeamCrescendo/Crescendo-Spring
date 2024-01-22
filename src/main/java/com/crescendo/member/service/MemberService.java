package com.crescendo.member.service;

import com.crescendo.member.dto.request.DuplicateCheckDTO;
import com.crescendo.member.dto.request.ModifyMemberRequestDTO;
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
        System.out.println("dto = " + dto);
        String account = dto.getAccount();
        if(memberRepository.existsById(account)){
            log.warn("계정이 중복되었습니다!! -{}.", account);
            throw new DuplicatedAccountException("중복된 계정입니다!!");
        }
        String email = dto.getEmail();
        if(memberRepository.existsByEmail(email)){
            log.warn("이메일이 중복되었습니다!! -{}.", email);
            throw new DuplicateEmailException("중복된 이메일입니다!!");
        }
        String userName = dto.getUserName();
        if(memberRepository.existsByUserName(userName)){
            log.warn("계정명이 중복되었습니다!! -{}.", userName);
            throw new DuplicateUserNameException("중복된 계정명입니다!!");
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

        Member foundMember = memberRepository.getOne(account);
        if(foundMember == null){
            throw new NoMatchAccountException("일치하는 계정이 없습니다");
        }
        return foundMember;
    }

    public boolean modifyUser(ModifyMemberRequestDTO dto){
        Member foundMember = memberRepository.getOne(dto.getAccount());
        if(foundMember == null){
            throw new NoMatchAccountException("일치하는 계정이 없습니다");
        }
        if(foundMember.getEmail().equals(dto.getEmail())){
            foundMember.setEmail(dto.getEmail());
        }else{
            boolean emailFlag = memberRepository.existsByEmail(dto.getEmail());
            if(emailFlag){
                throw new DuplicateEmailException("이메일이 이미 있습니다");
            }else {
                foundMember.setEmail(dto.getEmail());
            }
        }
        if(foundMember.getUserName().equals(dto.getUserName())){
            foundMember.setUserName(dto.getUserName());
        }else{
            boolean userFlag = memberRepository.existsByUserName(dto.getUserName());
            if(userFlag){
                throw new DuplicateUserNameException("유저명이 이미 있습니다");
            }else {
                foundMember.setUserName(dto.getUserName());
            }
        }
        foundMember.setPassword(encoder.encode(dto.getPassword()));
        return true;
    }

    // 중복 검사
    public boolean duplicateCheck(DuplicateCheckDTO dto){
        if(dto == null){
            throw new NoDuplicateCheckArgumentException("타겟과 벨류를 정확히 주세요");
        }
        if(dto.getTarget().equals("email")){
            boolean contains = dto.getValue().contains("@");
            if(!contains){
                throw new NoDuplicateCheckArgumentException("이메일 형식으로 적어주세요");
            }
            return memberRepository.existsByEmail(dto.getValue());
        }else if(dto.getTarget().equals("account")){
            return memberRepository.existsByAccount(dto.getValue());
        }else if(dto.getTarget().equals("userName")) {
            return memberRepository.existsByUserName(dto.getValue());
        }else{
            throw new NoDuplicateCheckArgumentException("타겟을 정확히 정해라");
        }

    }

}
