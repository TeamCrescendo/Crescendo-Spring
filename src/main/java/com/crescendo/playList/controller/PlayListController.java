package com.crescendo.playList.controller;

import com.crescendo.board.dto.response.BoardListResponseDTO;
import com.crescendo.playList.dto.requestDTO.PlayListRequestDTO;
import com.crescendo.playList.dto.responseDTO.PlayListResponseDTO;
import com.crescendo.playList.service.PlayListService;
import com.crescendo.score.dto.request.CreateScoreRequestDTO;
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
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/playList")
public class PlayListController {

    private final PlayListService playListService;

    @PostMapping()
    public ResponseEntity<?> savePlayList(@Validated @RequestBody PlayListRequestDTO dto
            , BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.toString());
        }
        try{
            boolean b = playListService.myPlayList(dto);
            return ResponseEntity.ok().body(b);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(BoardListResponseDTO.builder().error(e.getMessage()).build());
        }
    }
    //playList 목록 조회 요청
    @GetMapping("/{account}/{plId}")
    public ResponseEntity<?> retrievePlayList(@PathVariable String account, @PathVariable Long plId){
        if(account == null || plId ==null){
            return ResponseEntity.badRequest().body("계정과 재생목록을 다시 확인해주세요.");
        }
        try {
            List<PlayListResponseDTO> myPlayLists = playListService.getMyPlayLists(account, plId);
            return ResponseEntity.ok().body(myPlayLists);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
