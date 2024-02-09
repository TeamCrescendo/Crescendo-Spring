package com.crescendo.member.service;

import com.crescendo.allPlayList.repository.AllPlayListRepository;
import com.crescendo.aws.S3Service;
import com.crescendo.member.dto.request.*;
import com.crescendo.member.dto.response.LoginUserResponseDTO;
import com.crescendo.member.entity.Member;
import com.crescendo.member.exception.*;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.member.util.FileUtil;
import com.crescendo.member.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class MemberService {

    private final S3Service s3Service;
    private final MemberRepository memberRepository;
    private final AllPlayListRepository allPlayListRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;
    @Value("${file.upload.root-path}")
    private String rootPath;


    // 회원 가입 처리
    public boolean signUp(SignUpRequestDTO dto) throws IOException {
        if (dto == null) {
            log.warn("회원정보가 없습니다");
            throw new NoRegisteredArgumentsException("회원가입 입력정보가 없습니다!");
        }
        System.out.println("dto = " + dto);
        String account = dto.getAccount();
        if (memberRepository.existsById(account)) {
            log.warn("계정이 중복되었습니다!! -{}.", account);
            throw new DuplicatedAccountException("중복된 계정입니다!!");
        }
        String email = dto.getEmail();
        if (memberRepository.existsByEmail(email)) {
            log.warn("이메일이 중복되었습니다!! -{}.", email);
            throw new DuplicateEmailException("중복된 이메일입니다!!");
        }
        String userName = dto.getUserName();
        if (memberRepository.existsByUserName(userName)) {
            log.warn("계정명이 중복되었습니다!! -{}.", userName);
            throw new DuplicateUserNameException("중복된 계정명입니다!!");
        }

        if(dto.getProfileImage().getSize() != 0 && dto.getProfileImage() != null){
            String upload = FileUtil.upload(dto.getProfileImage(), rootPath);
            //aws 처리하고 파일패스 돌려받기
            String aws_path = s3Service.uploadToS3Bucket(dto.getProfileImage().getBytes(), upload);
            Member save = memberRepository.save(dto.toEntity(encoder));
            save.setProfileImageUrl(aws_path);
            log.info("회원가입 성공!! saved user - {}", save);
            return true;
        }


        Member save = memberRepository.save(dto.toEntity(encoder));
        log.info("회원가입 성공!! saved user - {}", save);
        return true;
    }


    //구글 로그인 처리
    public boolean signUp(GoogleSignUpRequestDTO dto) {
        if (dto == null) {
            log.warn("회원정보가 없습니다");
            throw new NoRegisteredArgumentsException("회원가입 입력정보가 없습니다!");
        }
        System.out.println("dto = " + dto);
        String account = dto.getAccount();
        if (memberRepository.existsById(account)) {
            log.warn("계정이 중복되었습니다!! -{}.", account);
            throw new DuplicatedAccountException("중복된 계정입니다!!");
        }
        String email = dto.getEmail();
        if (memberRepository.existsByEmail(email)) {
            log.warn("이메일이 중복되었습니다!! -{}.", email);
            throw new DuplicateEmailException("중복된 이메일입니다!!");
        }
        String userName = dto.getUserName();
        if (memberRepository.existsByUserName(userName)) {
            log.warn("계정명이 중복되었습니다!! -{}.", userName);
            throw new DuplicateUserNameException("중복된 계정명입니다!!");
        }



        Member save = memberRepository.save(dto.toEntity(encoder));

        log.info("회원가입 성공!! saved user - {}", save);
        return true;

    }




    // 로그인 처리
    public LoginUserResponseDTO signIn(SignInRequestDTO dto) {
        if (dto == null) {
            log.warn("로그인 정보가 없습니다.");
            throw new NoLoginArgumentsException("로그인 정보가 없습니다");
        }
        Member foundMember = memberRepository.getOne(dto.getAccount());
        if (foundMember == null) {
            log.warn("일치하는 계정이 없습니다.");
            throw new NoMatchAccountException("일치하는 계정이 없습니다");
        }
        String encodedPassword = foundMember.getPassword();
        String rawPassword = dto.getPassword();
        if (!encoder.matches(rawPassword, encodedPassword)) {
            log.warn("비밀번호가 틀렸습니다.");
            throw new IncorrectPasswordException("비밀번호가 틀렸습니다.");
        }

        String token = null;
        if(dto.isAutoLogin()==true){
            token=tokenProvider.createAutiLoginJwt(foundMember);
        }else{
            token=tokenProvider.createToken(foundMember);

        }
        return new LoginUserResponseDTO(foundMember, token);
    }


    //구글 로그인 처리
    public LoginUserResponseDTO signIn(GoogleSignInRequestDTO dto) {
        if (dto == null) {
            log.warn("로그인 정보가 없습니다.");
            throw new NoLoginArgumentsException("로그인 정보가 없습니다");
        }
        Member foundMember = memberRepository.getOne(dto.getAccount());
        if (foundMember == null) {
            log.warn("일치하는 계정이 없습니다.");
            throw new NoMatchAccountException("일치하는 계정이 없습니다");
        }
        String encodedPassword = foundMember.getPassword();
        String token = tokenProvider.createToken(foundMember);
        log.info("구글 로그인 성공");
        return new LoginUserResponseDTO(foundMember, token);
    }



    public Member findUser(String account) {
        Member foundMember = memberRepository.getOne(account);
        if (foundMember == null) {
            throw new NoMatchAccountException("일치하는 계정이 없습니다");
        }
        return foundMember;
    }

    // 유저 수정
    public boolean modifyUser(ModifyMemberRequestDTO dto) {
        Member foundMember = memberRepository.getOne(dto.getAccount());
        if (foundMember == null) {
            throw new NoMatchAccountException("일치하는 계정이 없습니다");
        }
        if (foundMember.getEmail().equals(dto.getEmail())) {
            foundMember.setEmail(dto.getEmail());
        } else {
            boolean emailFlag = memberRepository.existsByEmail(dto.getEmail());
            if (emailFlag) {
                throw new DuplicateEmailException("이메일이 이미 있습니다");
            } else {
                foundMember.setEmail(dto.getEmail());
            }
        }
        if (foundMember.getUserName().equals(dto.getUserName())) {
            foundMember.setUserName(dto.getUserName());
        } else {
            boolean userFlag = memberRepository.existsByUserName(dto.getUserName());
            if (userFlag) {
                throw new DuplicateUserNameException("유저명이 이미 있습니다");
            } else {
                foundMember.setUserName(dto.getUserName());
            }
        }
        if (!dto.getPassword().isEmpty()) {
            foundMember.setPassword(encoder.encode(dto.getPassword()));
        }
        if (dto.getProfileImage() != null && dto.getProfileImage().getSize() != 0) {
            String upload = FileUtil.upload(dto.getProfileImage(), rootPath);
            foundMember.setProfileImageUrl(upload);
        }
        return true;
    }

    // 아이디와 이메일이 맞는지 검증하는 메서드
    public boolean verifyAccountAndEmail(ModifyPasswordRequestDTO dto) {
        // DTO에서 받은 account 값이 null이 아닌지 확인
        if (dto.getAccount() == null) {
            throw new IllegalArgumentException("계정이 제공되지 않았습니다.");
        }

        // DTO에서 받은 이메일과 일치하는 사용자 찾기
        Member member = memberRepository.getMemberByAccount(dto.getAccount());
        if (member == null) {
            throw new NoMatchAccountException("일치하는 계정을 찾을 수 없습니다.");
        }

        // DTO에서 받은 이메일과 사용자의 이메일을 비교하여 일치하지 않으면 예외를 던짐
        if (!member.getEmail().equals(dto.getEmail())) {
            throw new NoMatchAccountException("이메일이 일치하지 않습니다.");
        }

        return true;
    }

    // 비밀번호 변경 메서드
    public void modifyPassword(ModifyPasswordRequestDTO dto) {
        // 아이디와 이메일이 맞는지 검증
        if (verifyAccountAndEmail(dto)) {
            // 새로운 비밀번호를 해싱하여 저장
            String password = encoder.encode(dto.getPassword());
            Member member = memberRepository.getMemberByAccount(dto.getAccount());
            member.setPassword(password);
            memberRepository.save(member);
        }
    }


    // 중복 검사
    public boolean duplicateCheck(DuplicateCheckDTO dto) {
        if (dto == null) {
            throw new NoDuplicateCheckArgumentException("타겟과 벨류를 정확히 주세요");
        }
        if (dto.getTarget().equals("email")) {
            boolean contains = dto.getValue().contains("@");
            if (!contains) {
                throw new NoDuplicateCheckArgumentException("이메일 형식으로 적어주세요");
            }
            return memberRepository.existsByEmail(dto.getValue());
        } else if (dto.getTarget().equals("account")) {
            return memberRepository.existsByAccount(dto.getValue());
        } else if (dto.getTarget().equals("userName")) {
            return memberRepository.existsByUserName(dto.getValue());
        } else {
            throw new NoDuplicateCheckArgumentException("타겟을 정확히 정해라");
        }
    }

    // 다운로드 회수 줄이기
    public int download(String account) {
        if (!memberRepository.existsById(account)) {
            throw new NoMatchAccountException("해당 하는 계정명이 없습니다");
        }
        Member member = memberRepository.getOne(account);
        Integer userDownloadChance = member.getUserDownloadChance();

        if (userDownloadChance == 0) {
            throw new NoDownloadChanceException("다운로드 기회가 없습니다");
        }
        log.info("여기까지 왓나용");
        member.setUserDownloadChance(member.getUserDownloadChance() - 1);
        return member.getUserDownloadChance();
    }


    // 프로필 이미지 저장
    public boolean uploadProfileImage(ProfileUploadRequestDTO dto) {

        String upload = FileUtil.upload(dto.getFile(), rootPath);

        boolean b = memberRepository.existsByAccount(dto.getAccount());
        if (!b) {
            throw new NoMatchAccountException("해당 회원은 없습니다");
        }

        Member foundMember = memberRepository.getOne(dto.getAccount());

        foundMember.setProfileImageUrl(upload);
        return true;
    }

    public void googleLogin(String code, String registrationId) {
        System.out.println("code = " + code);
        System.out.println("registrationId = " + registrationId);
    }


    //유저를 검색하고 countCheck 를 진행하는 서비스
    public void findUserAndCountCheck(Member member) {
        //2.카운트 다운
        coutDownDownload(member);


    }

    //다운로드 2-> 악보생성시 사용함
    public void coutDownDownload(Member member) {

        Integer userDownloadChance = member.getUserDownloadChance();
        member.setUserDownloadChance(userDownloadChance-1);
    }



    public Boolean deleteUser(String account) {
        //유저찾기-> findUser에서 검사해줌
        Member user = findUser(account);
        //유저에 관련된 모든 플레이리스트 삭제하기
        boolean isDelete = allPlayListRepository.deleteByAccount(user);
        if(isDelete){
            memberRepository.delete(user);
            return true;
        }
        return false;
        

    }
}
