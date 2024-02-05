package com.crescendo.allPlayList.controller;

import com.crescendo.allPlayList.dto.request.AllPlayListRequestDTO;
import com.crescendo.allPlayList.dto.response.AllPlayListResponseDTO;
import com.crescendo.allPlayList.dto.response.AllPlayResponseDTO;
import com.crescendo.allPlayList.service.AllPlayListService;
import com.crescendo.member.util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/allPlayList")
public class AllPlayController {

    private final AllPlayListService allPlayListService;

    //AllPlayList 등록 요청
    @PostMapping("/createAllPlayList")
    public ResponseEntity<?> createPlayList(
            @Validated
            @RequestBody AllPlayListRequestDTO dto,
            BindingResult result,
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo)
    {
        if (result.hasErrors()) {
            log.warn("DTO 검증 에러 입니다. : {}", result.getFieldError());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        try {
            AllPlayResponseDTO allPlayResponseDTO = allPlayListService.create(dto, tokenUserInfo.getAccount());
            return ResponseEntity.ok().body(allPlayResponseDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(AllPlayListResponseDTO.builder());
        }

    }

    //AllPlayList 조회 요청
    @GetMapping
    public ResponseEntity<?>  retrievePlayList(){
        log.info("/api/PlayList GET!!!!!");

        AllPlayResponseDTO retrieve = allPlayListService.retrieve();
        return ResponseEntity.ok().body(retrieve);
    }

    //AllPlayList 수정 요청
    @RequestMapping(method = {PUT, PATCH}, path = "/modify")
    public ResponseEntity<?> AllPlayListUpdate(@RequestBody AllPlayListRequestDTO dto){
        try{
            boolean update = allPlayListService.modifyAllPlayList(dto);
            return ResponseEntity.ok().body(update);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //AllPlayList 삭제 요청
    @DeleteMapping("/{plId}")
    public ResponseEntity<?> deletePlayList(@PathVariable Long plId){

        log.info("/api/PlayList DELETE !!!");

        if (plId == null || plId.equals("")){
            return ResponseEntity.badRequest().body(AllPlayListResponseDTO.builder().build());
        }
        try{
            AllPlayResponseDTO delete = allPlayListService.delete(plId);
            return ResponseEntity.ok().body(delete);
        }catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(AllPlayResponseDTO.builder().build());
        }
    }
}



