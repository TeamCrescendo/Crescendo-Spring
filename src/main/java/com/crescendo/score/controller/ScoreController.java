package com.crescendo.score.controller;

import com.crescendo.member.entity.Member;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.service.MemberService;
import com.crescendo.member.util.TokenUserInfo;
import com.crescendo.post_message.dto.request.CreateNotationRequestDTO;
import com.crescendo.post_message.dto.response.NotationResPonseDTO;
import com.crescendo.score.dto.request.AiMusicRequestDTO;
import com.crescendo.score.dto.request.CreateAiScoreRequestDTO;
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
import org.springframework.web.multipart.MultipartFile;

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
        Member user = memberService.findUser(tokenUser.getAccount());
        int usrCountDown = memberService.getUsrCountDown(user);
        if(usrCountDown<=0){
            return ResponseEntity.status(403).body("하루 다운횟수초과");

        }
        log.info("/api/score POST {}", dto.getUrl());

        //python에 전달할 json 데이터 포장 url ,account
        CreateNotationRequestDTO requestDTO = CreateNotationRequestDTO.builder()
                .url(dto.getUrl())
                .account(tokenUser.getAccount())
                .build();
        log.info("creationNoationDTO:{}",requestDTO);


        NotationResPonseDTO notationResPonseDTO = scoreService.postToPython(requestDTO);
        if(notationResPonseDTO!=null){
            HttpHeaders headers = new HttpHeaders();
        //헤더에 추가해야하는건 scoreid임
        headers.add("score-id", String.valueOf(notationResPonseDTO.getScoreNo()));
        //헤더 정보를 이용해서 pdf 라는 걸 다시한번 인지시켜주기
        headers.add("content-type", "application/pdf");
        memberService.findUserAndCountCheck(user);
        return ResponseEntity.ok()
                .headers(headers)
                .body(notationResPonseDTO.getPdfNotation());
        }

        return ResponseEntity.status(500).body("안되요..");



    }
    @PostMapping("/ai")
    private ResponseEntity<?> youtubeLink(@AuthenticationPrincipal TokenUserInfo tokenUser, @RequestBody AiMusicRequestDTO dto){
        // 회원도 받아서 인증해야함.
        Member user = memberService.findUser(tokenUser.getAccount());
        //카운트가 안되면 반납
        int usrCountDown = memberService.getUsrCountDown(user);
        if(usrCountDown<=0){
            return ResponseEntity.status(403).body("하루 다운횟수초과");

        }

        //python에 전달할 json 데이터 포장 url ,account
        CreateAiScoreRequestDTO requestDTO = CreateAiScoreRequestDTO.builder()
                .prompt(dto.getPrompt())
                .duration(dto.getDuration())
                .account(tokenUser.getAccount())
                .build();


        byte[] aiMp3 = scoreService.postToPython(requestDTO);
        if(aiMp3!=null){
            memberService.findUserAndCountCheck(user);// 카운트 깍는 서비스
            HttpHeaders headers = new HttpHeaders();
            headers.add("content-type", "audio/mp3");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(aiMp3);
        }

        return ResponseEntity.status(500).body("안되요..");



    }



}