package com.crescendo.restore.controller;


import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.restore.dto.response.RestoreWhetherResponseDTO;
import com.crescendo.restore.exception.ExistsInRestoreException;
import com.crescendo.restore.service.RestoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restore")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class RestoreController {
    private final RestoreService restoreService;


    // 삭제 요청 하기
    @PostMapping("/{account}")
    public ResponseEntity<?> save(@PathVariable String account){
        //log.info("/api/restore Post!!");
        if(account.isEmpty()){
            return ResponseEntity.badRequest().body("제대로된 계정명을 주세요");
        }

        try{
            boolean add = restoreService.add(account);
            return ResponseEntity.ok().body(add);
        }catch (ExistsInRestoreException | NoMatchAccountException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 삭제 여부 확인
    @GetMapping
    public ResponseEntity<?> find(String account){
        log.info("/api/restore GET!! {}", account);
        if(account.isEmpty()){
            return ResponseEntity.badRequest().body("제대로된 계정명을 주세요");
        }

        try {
            RestoreWhetherResponseDTO restoreWhetherResponseDTO = restoreService.find(account);
            return ResponseEntity.ok().body(restoreWhetherResponseDTO);
        }catch (ExistsInRestoreException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    // 삭제 취소 요청 하기
    @DeleteMapping
    public ResponseEntity<?> delete(String restoreNo){
        log.info("/api/restore Delete!! {}", restoreNo);
        if(restoreNo.isEmpty()){
            return ResponseEntity.badRequest().body("제대로된 계정명을 주세요");
        }

        try{
            boolean cancel = restoreService.cancel(restoreNo);
            return ResponseEntity.ok().body(cancel);
        }catch (ExistsInRestoreException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
