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
    @GetMapping("/{plId}")
    public ResponseEntity<?> retrievePlayList(@AuthenticationPrincipal TokenUserInfo tokenUserInfo, @PathVariable Long plId){
        if(tokenUserInfo.getAccount() == null || plId ==null){
            return ResponseEntity.badRequest().body("계정과 재생목록을 다시 확인해주세요.");
        }
        try {
            List<PlayListResponseDTO> myPlayLists = playListService.getMyPlayLists(tokenUserInfo.getAccount(), plId);
            return ResponseEntity.ok().body(myPlayLists);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //playList삭제 요청
    @DeleteMapping("/{account}/{plId}/{plNo}")
    public ResponseEntity<?> deletePlayList(@AuthenticationPrincipal TokenUserInfo tokenUserInfo, @PathVariable Long plId, @PathVariable Long plNo){
        if(tokenUserInfo.getAccount() ==null || plId == null || plNo ==null){
            return ResponseEntity.badRequest().body("계정정보와 재생목록과 플레이리스트를 다시 확인해 주세요!");
        }
        try{
            boolean delete = playListService.deleteMyPlayList(tokenUserInfo.getAccount(), plId, plNo);
            if(delete) {
                return ResponseEntity.ok().body("플레이리스트 삭제에 성공 했습니다.");
            }else{
                return ResponseEntity.badRequest().body("플레이 리스트 삭제에 실패 했습니다.");
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//    //내가 추가 하고 싶은 악보가 있는데, playList 안에 그 악보가 있는지 확인 요청
//    @GetMapping("/check")
//    public ResponseEntity<?> checkPlayList(@AuthenticationPrincipal TokenUserInfo tokenUserInfo, @PathVariable Long plId, @PathVariable int scoreNo){
//        try{
//            boolean check = playListService.checkMyPlayList(tokenUserInfo.getAccount(), plId, scoreNo);
//            if(check){
//                return ResponseEntity.ok().body("악보를 저장 하려고 하는데 이미 playList안에 악보가 있습니다!");
//            }else{
//                return ResponseEntity.ok().body("악보를 저장 하려고 하는데 playList안에는 해당하는 악보는 없습니다!");
//            }
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body("나의 플레이 리스트를 확인 하는 도중 오류 발생");
//        }
//    }
}
