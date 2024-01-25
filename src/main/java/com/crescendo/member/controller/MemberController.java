package com.crescendo.member.controller;

import com.crescendo.member.dto.request.ModifyMemberRequestDTO;
import com.crescendo.member.dto.request.ProfileUploadRequestDTO;
import com.crescendo.member.dto.response.LoginUserResponseDTO;
import com.crescendo.member.entity.Member;
import com.crescendo.member.exception.DuplicateEmailException;
import com.crescendo.member.exception.DuplicateUserNameException;
import com.crescendo.member.exception.NoDownloadChanceException;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.service.MemberService;
import com.crescendo.member.util.FileUtil;
import com.crescendo.member.util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class MemberController {
    @Value("${file.upload.root-path}")
    private String rootPath;

    private final MemberService memberService;

    // 유저 다운로드 회수 차감
    @GetMapping("/download")
    public ResponseEntity<?> downloadScore(@AuthenticationPrincipal TokenUserInfo tokenUserInfo) {
        log.info("/api/member/download");
        if (tokenUserInfo.getAccount().isEmpty()) {
            return ResponseEntity.badRequest().body("계정명을 정확히 적어주세요");
        }
        try {
            int download = memberService.download(tokenUserInfo.getAccount());
            return ResponseEntity.ok().body(download);
        } catch (NoMatchAccountException | NoDownloadChanceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 유저 프로필 이미지 등록
    @PostMapping("/profile")
    public ResponseEntity<?> uploadProfileImage(ProfileUploadRequestDTO dto) {
        log.info("api/member/profile Post!!");
        try{
            boolean b = memberService.uploadProfileImage(dto);
            return ResponseEntity.ok().body(b);
        }catch (NoMatchAccountException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 회원 정보 수정
    @RequestMapping(method = {PUT, PATCH}, path = "/modify")
    public ResponseEntity<?> updateUser(@Validated ModifyMemberRequestDTO dto, BindingResult result){
        log.info("dto: {}", dto.toString());
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.toString());
        }
        try{
            boolean flag = memberService.modifyUser(dto);
            return ResponseEntity.ok().body(flag);
        }catch (NoMatchAccountException | DuplicateEmailException | DuplicateUserNameException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 유저 정보 찾기
    @GetMapping
    public ResponseEntity<?> compareTo(@AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        Member user = memberService.findUser(tokenUserInfo.getAccount());
        LoginUserResponseDTO loginUserResponseDTO = new LoginUserResponseDTO(user);
        return ResponseEntity.ok().body(loginUserResponseDTO);
    }
}
