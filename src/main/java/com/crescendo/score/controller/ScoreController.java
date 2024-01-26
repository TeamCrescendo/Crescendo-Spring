package com.crescendo.score.controller;

import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.score.dto.request.CreateScoreRequestDTO;
import com.crescendo.score.dto.request.YoutubeLinkRequestDTO;
import com.crescendo.score.dto.response.FindByAccountScoreResponseDTO;
import com.crescendo.score.exception.InvalidGenreException;
import com.crescendo.score.exception.NoArgumentException;
import com.crescendo.score.service.ScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/score")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class ScoreController {
    private final ScoreService scoreService;

    // 악보 추가 하기
    @PostMapping
    public ResponseEntity<?> save(@Validated @RequestBody CreateScoreRequestDTO dto, BindingResult result){
        log.info("/api/score POST!!");
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.toString());
         }
        try{
            boolean flag = scoreService.createScore(dto);
            return ResponseEntity.ok().body(flag);
        }catch (InvalidGenreException | NoMatchAccountException | NoArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> findAllByAccount(String account){
        if(account == null || account.isEmpty()){
            return ResponseEntity.badRequest().body("계정명 제대로 주세요");
        }
        try{
            List<FindByAccountScoreResponseDTO> allByAccount = scoreService.findAllByAccount(account);
            return ResponseEntity.ok().body(allByAccount);
        }catch (NoMatchAccountException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    private ResponseEntity<?> delete(int scoreId){
        if(scoreId == 0){
            return ResponseEntity.badRequest().body("악보아이디 제대로 주세요");
        }
        try{
            boolean delete = scoreService.delete(scoreId);
            return ResponseEntity.ok().body(delete);
        }catch (NoArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // 유튜브 링크 주소 받아주는 URL
    @PostMapping("/youtube")
    private ResponseEntity<?> youtubeLink(@RequestBody YoutubeLinkRequestDTO dto){
        // 회원도 받아서 인증해야함.
        log.info("/api/score POST {}", dto.getUrl());

        // 서비스 한테 파이썬으로 값 보내야함 ..
        String s = scoreService.postToPython(dto.getUrl());
        return ResponseEntity.ok().body(s);
    }
}