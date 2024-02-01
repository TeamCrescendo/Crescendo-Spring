package com.crescendo.board.controller;

import com.crescendo.board.dto.request.BoardModifyRequestDTO;
import com.crescendo.board.dto.request.BoardRequestDTO;
import com.crescendo.board.dto.response.BoardListResponseDTO;
import com.crescendo.board.entity.Board;
import com.crescendo.board.entity.Dislike;
import com.crescendo.board.entity.Like;
import com.crescendo.board.service.BoardService;
import com.crescendo.likeAndDislike.dto.request.LikeAndDislikeRequestDTO;
import com.crescendo.member.util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;


    //Board 등록 요청
    @PostMapping("/createBoard")
    public ResponseEntity<?> createBoard(
            @Validated
            @RequestBody BoardRequestDTO dto,
            BindingResult result,
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo
            ){
        log.info("123312333123123123");
        if(result.hasErrors()){
            log.warn("DTO 검증 에러 입니다. : {}",result.getFieldError());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        try{
            BoardListResponseDTO dtoList = boardService.create(dto,tokenUserInfo.getAccount());
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
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardNo,@AuthenticationPrincipal TokenUserInfo userInfo){

        log.info("/api/board/{} DELETE!!",boardNo);

        if (boardNo == null || boardNo.equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(BoardListResponseDTO.builder().error("boardNo는 공백 일 수 없습니다!").build());
        }

        try{
            BoardListResponseDTO dtoList = boardService.delete(userInfo.getAccount());
            return ResponseEntity.ok().body(dtoList);
        }catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(BoardListResponseDTO.builder().error(e.getMessage()).build());
        }
    }

    //좋아요 싫어요 처리\

    @PostMapping("/likeAndDislike")
    public ResponseEntity<?> likeAndDisLike(@RequestBody LikeAndDislikeRequestDTO dto){
    boardService.LikeAndDislike(dto);
    return ResponseEntity.ok().body("성공적으로 좋아요와 싫어요 연결 성공");
    }


    //PDF파일을 byte로 바꾸는 처리
    @GetMapping("/{boardId}")
    public  ResponseEntity<?>downloadPdf(@PathVariable Long boardId){
        ResponseEntity<byte[]> boardPdf = boardService.getBoardPdf(boardId);
        HttpHeaders headers=  boardPdf.getHeaders();

        //클라이언트에게 HttpHeaders와 함께 PDF를 전송
        return ResponseEntity.ok().headers(headers).body(boardPdf.getBody());

    }
}
