package com.crescendo.member.controller;

import com.crescendo.member.dto.request.ProfileUploadRequestDTO;
import com.crescendo.member.exception.NoDownloadChanceException;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.service.MemberService;
import com.crescendo.member.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    @PostMapping("/download/{account}")
    public ResponseEntity<?> downloadScore(@PathVariable String account) {
        if (account.isEmpty()) {
            return ResponseEntity.badRequest().body("계정명을 정확히 적어주세요");
        }
        try {
            int download = memberService.download(account);
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
}
