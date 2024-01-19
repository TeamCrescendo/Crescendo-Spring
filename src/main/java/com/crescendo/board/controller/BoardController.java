package com.crescendo.board.controller;

import com.crescendo.board.dto.request.BoardModifyRequestDTO;
import com.crescendo.board.dto.request.BoardRequestDTO;
import com.crescendo.board.dto.response.BoardListResponseDTO;
import com.crescendo.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    //Board 등록 요청
    @PostMapping("/createBoard")
    public ResponseEntity<?> createBoard(
            @Validated
            @RequestBody BoardRequestDTO dto,
            BindingResult result
            ){
        if(result.hasErrors()){
            log.warn("DTO 검증 에러 입니다. : {}",result.getFieldError());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        try{
            BoardListResponseDTO dtoList = boardService.create(dto);
            return ResponseEntity.ok().body(dtoList);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(BoardListResponseDTO.builder().error(e.getMessage()).build());
        }

    }

    //Board 목록 조회 요청
    @GetMapping
    public ResponseEntity<?> retrieveBoardList(){
        log.info("/api/board GET!!");

        BoardListResponseDTO retrieve = boardService.retrieve();
        return ResponseEntity.ok().body(retrieve);
    }

    //Board 수정
    @RequestMapping(method = {PUT, PATCH}, path = "/modify")
    public ResponseEntity<?> UpdateBoard(@RequestBody BoardModifyRequestDTO dto){

        try{
            boolean update = boardService.modifyBoard(dto);
            return ResponseEntity.ok().body(update);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Board 삭제 요청
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardNo){

        log.info("/api/board/{} DELETE!!",boardNo);

        if (boardNo == null || boardNo.equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(BoardListResponseDTO.builder().error("boardNo는 공백 일 수 없습니다!").build());
        }

        try{
            BoardListResponseDTO dtoList = boardService.delete(boardNo);
            return ResponseEntity.ok().body(dtoList);
        }catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(BoardListResponseDTO.builder().error(e.getMessage()).build());
        }
    }

}
