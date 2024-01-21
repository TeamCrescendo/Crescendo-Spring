package com.crescendo.score.controller;

import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.score.dto.request.CreateScoreRequestDTO;
import com.crescendo.score.exception.InvalidGenreException;
import com.crescendo.score.exception.NoArgumentException;
import com.crescendo.score.service.ScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/score")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class ScoreController {
    private final ScoreService scoreService;

    @PostMapping
    public ResponseEntity<?> save(@Validated @RequestBody CreateScoreRequestDTO dto, BindingResult result){
        log.info("/api/score POST!!");
        System.out.println("dto = " + dto);
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



}
