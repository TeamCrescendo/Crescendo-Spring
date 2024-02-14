package com.crescendo.member.controller;

import com.crescendo.member.dto.request.ModifyMemberRequestDTO;
import com.crescendo.member.dto.request.ModifyPasswordRequestDTO;
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



    // 아이디와 이메일이 맞는지 검증하는 엔드포인트
    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestBody ModifyPasswordRequestDTO dto,
                                         BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        try {
            boolean isMatch = memberService.verifyAccountAndEmail(dto);
            return ResponseEntity.ok().body(isMatch);
        } catch (NoMatchAccountException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 비밀번호 변경하는 엔드포인트
    @PutMapping("/modifyPassword")
    public ResponseEntity<?> modifyPassword( @RequestBody ModifyPasswordRequestDTO dto,
                                            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        try {
            memberService.modifyPassword(dto);
            return ResponseEntity.ok().body("비밀번호 변경 완료");
        } catch (NoMatchAccountException e) {
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

    //유저 정보삭제
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        Boolean isDelete=memberService.deleteUser(tokenUserInfo.getAccount());
        if(isDelete)
            return ResponseEntity.ok().body("계정정보가 성공적으로 삭제 되었습니다.");
        return  ResponseEntity.badRequest().body("삭제에 실패했습니다! 다시시도해주세요!");

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
}
