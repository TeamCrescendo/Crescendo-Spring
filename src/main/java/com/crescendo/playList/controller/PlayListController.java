package com.crescendo.playList.controller;

import com.crescendo.board.dto.response.BoardListResponseDTO;
import com.crescendo.member.util.TokenUserInfo;
import com.crescendo.playList.dto.requestDTO.PlayListRequestDTO;
import com.crescendo.playList.dto.responseDTO.PlayListResponseDTO;
import com.crescendo.playList.service.PlayListService;
import com.crescendo.score.dto.request.CreateScoreRequestDTO;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            , BindingResult result,
                                          @AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.toString());
        }
        try{
            boolean b = playListService.myPlayList(dto, tokenUserInfo.getAccount());
            return ResponseEntity.ok().body(b);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(BoardListResponseDTO.builder().error(e.getMessage()).build());
        }
    }
    //playList 목록 조회 요청
    @GetMapping()
    public ResponseEntity<?> findMyPlayList(@AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        List<PlayListResponseDTO> myPlayList = playListService.findMyPlayList(tokenUserInfo.getAccount());
        return ResponseEntity.ok().body(myPlayList);
    }


//    //playList삭제 요청
//    @DeleteMapping("{plNo}")
//    public ResponseEntity<?> deletePlayList(@AuthenticationPrincipal TokenUserInfo tokenUserInfo,@PathVariable Long plNo){
//        List<PlayListResponseDTO> playListResponseDTOS = playListService.deleteMyPlayListAndRetrieve(tokenUserInfo.getAccount(), plNo);
//        return ResponseEntity.ok().body(playListResponseDTOS);
//    }

}
