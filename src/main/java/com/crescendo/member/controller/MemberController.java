package com.crescendo.member.controller;

import com.crescendo.member.exception.NoDownloadChanceException;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class MemberController {
    private final MemberService memberService;

    // 유저 다운로드 회수 차감
    @PostMapping("/download/{account}")
    public ResponseEntity<?> downloadScore(@PathVariable String account){
        if (account.isEmpty()){
            return ResponseEntity.badRequest().body("계정명을 정확히 적어주세요");
        }
        try{
            int download = memberService.download(account);
            return ResponseEntity.ok().body(download);
        }catch (NoMatchAccountException | NoDownloadChanceException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
