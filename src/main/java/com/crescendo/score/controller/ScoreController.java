package com.crescendo.score.controller;

import com.crescendo.member.entity.Member;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.service.MemberService;
import com.crescendo.member.util.TokenUserInfo;
import com.crescendo.post_message.dto.request.CreateNotationRequestDTO;
import com.crescendo.post_message.dto.response.NotationResPonseDTO;
import com.crescendo.score.dto.request.CreateScoreRequestDTO;
import com.crescendo.score.dto.request.YoutubeLinkRequestDTO;
import com.crescendo.score.dto.response.FindByAccountScoreResponseDTO;
import com.crescendo.score.entity.Score;
import com.crescendo.score.exception.InvalidGenreException;
import com.crescendo.score.exception.NoArgumentException;
import com.crescendo.score.service.ScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final MemberService memberService;

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
    private ResponseEntity<?> youtubeLink(@AuthenticationPrincipal TokenUserInfo tokenUser, @RequestBody YoutubeLinkRequestDTO dto){
        // 회원도 받아서 인증해야함.
//        boolean ischeck= memberService.findUserAndCountCheck(tokenUser.getAccount());
        log.info("/api/score POST {}", dto.getUrl());
        if(true) {
            // url 을 통해 받아온 pdf 파일
            // account, url을 api현대로 반환
            CreateNotationRequestDTO requestDTO = CreateNotationRequestDTO.builder()
                    .url(dto.getUrl())
                    .account(tokenUser.getAccount())
                    .build();
            log.info("creationNoationDTO:{}",requestDTO);


            NotationResPonseDTO notationResPonseDTO = scoreService.postToPython(requestDTO);

            HttpHeaders headers = new HttpHeaders();
            //헤더에 추가해야하는건 scoreid임
            headers.add("score-id", String.valueOf(notationResPonseDTO.getScoreNo()));
            //헤더 정보를 이용해서 pdf 라는 걸 다시한번 인지시켜주기
            headers.add("content-type", "application/pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(notationResPonseDTO.getPdfNotation());
        }
        else{
            return ResponseEntity.status(500).body("안되요..");

        }

    }


}