package com.crescendo.playList.controller;

import com.crescendo.board.dto.response.BoardListResponseDTO;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.util.TokenUserInfo;
import com.crescendo.playList.dto.requestDTO.PlayListDuplicateRequestDTO;
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

    // 플리 저장 요청
    @PostMapping
    public ResponseEntity<?> savePlayList(
            @Validated @RequestBody PlayListRequestDTO dto
            , BindingResult result
            , @AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.toString());
        }
        try{
            log.info("=======================================================");
            log.info("=======================================================");
            log.info("{}", dto);
            log.info("=======================================================");
            log.info("=======================================================");
            boolean b = playListService.myPlayList(dto, tokenUserInfo.getAccount());
            return ResponseEntity.ok().body(b);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(BoardListResponseDTO.builder().error(e.getMessage()).build());
        }
    }

    // 플리 중복 체크 여부
    @PostMapping("/duplicate")
    public ResponseEntity<?> duplicatedCheck(@RequestBody PlayListDuplicateRequestDTO dto) {
        try {
            log.info(dto.toString());
            List<Boolean> booleans = playListService.duplicationCheckDTO(dto);
            return ResponseEntity.ok().body(booleans);
        } catch (IllegalStateException e) {
            log.error("Error in duplicatedCheck", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error in duplicatedCheck", e);
            return ResponseEntity.internalServerError().body(BoardListResponseDTO.builder().error(e.getMessage()).build());
        }
    }


    //playList 목록 조회 요청
    @GetMapping()
    public ResponseEntity<?> findMyPlayList(@AuthenticationPrincipal TokenUserInfo tokenUserInfo, @PathVariable  Long plId){
        List<PlayListResponseDTO> myPlayList = playListService.findMyPlayList(tokenUserInfo.getAccount(), plId);
        return ResponseEntity.ok().body(myPlayList);
    }


    //playList삭제 요청
    @DeleteMapping("{plNo}")
    public ResponseEntity<?> deletePlayList(@AuthenticationPrincipal TokenUserInfo tokenUserInfo,@PathVariable Long plNo){
        boolean delete = playListService.deleteMyPlayListAndRetrieve(tokenUserInfo.getAccount(), plNo);
        return ResponseEntity.ok().body(delete);
    }

}
