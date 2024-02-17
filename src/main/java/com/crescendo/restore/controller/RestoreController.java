package com.crescendo.restore.controller;


import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.util.TokenUserInfo;
import com.crescendo.restore.dto.response.RestoreWhetherResponseDTO;
import com.crescendo.restore.exception.ExistsInRestoreException;
import com.crescendo.restore.service.RestoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restore")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class RestoreController {
    private final RestoreService restoreService;


    // 삭제 요청 하기
    @PostMapping
    public ResponseEntity<?> save(@AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        //log.info("/api/restore Post!!");
        if(tokenUserInfo.getAccount().isEmpty()){
            return ResponseEntity.badRequest().body("제대로된 계정명을 주세요");
        }

        try{
            boolean add = restoreService.add(tokenUserInfo.getAccount());
            return ResponseEntity.ok().body(add);
        }catch (ExistsInRestoreException | NoMatchAccountException e){
            return ResponseEntity.ok().body(false);
        }
    }

    // 삭제 여부 확인
    @GetMapping
    public ResponseEntity<?> find(@AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        log.info("/api/restore GET!! {}", tokenUserInfo.getAccount());
        if(tokenUserInfo.getAccount().isEmpty()){
            return ResponseEntity.badRequest().body("제대로된 계정명을 주세요");
        }

        try {
            RestoreWhetherResponseDTO restoreWhetherResponseDTO = restoreService.find(tokenUserInfo.getAccount());
            return ResponseEntity.ok().body(restoreWhetherResponseDTO);
        }catch (ExistsInRestoreException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 삭제 취소 요청 하기
    @DeleteMapping
    public ResponseEntity<?> delete(@AuthenticationPrincipal TokenUserInfo userInfo){
        log.info("/api/restore Delete!! {}", userInfo);
        if(userInfo.getUserId().isEmpty()){
            return ResponseEntity.badRequest().body("제대로된 계정명을 주세요");
        }

        try{
            boolean cancel = restoreService.cancel(userInfo.getAccount());
            return ResponseEntity.ok().body(cancel);
        }catch (ExistsInRestoreException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
