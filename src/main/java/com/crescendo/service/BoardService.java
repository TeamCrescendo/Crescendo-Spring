package com.crescendo.service;

import com.crescendo.dto.request.BoardRequestDTO;
import com.crescendo.dto.response.BoardListResponseDTO;
import com.crescendo.dto.response.BoardResponseDTO;
import com.crescendo.entity.Board;
import com.crescendo.entity.Member;
import com.crescendo.repository.BoardRepository;
import com.crescendo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional // !! JPA 사용시 필수!!
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


    //board에 등록
    public BoardListResponseDTO create(BoardRequestDTO dto) {
        Member member = memberRepository.getOne(dto.getAccount());
        if(member == null) {
            return null;
        }
        Board build = Board.builder().boardTitle(dto.getBoardTitle()).member(member).build();
        boardRepository.save(build);
        log.info("새로운 보드를 내 마음속에 저★장★ : {}", dto.getBoardTitle());

        return retrieve();
    }

    //board 불러오기
    public BoardListResponseDTO retrieve() {
        List<Board> board = boardRepository.findAll();

        List<BoardResponseDTO> dtoList = board.stream()
                .map(BoardResponseDTO::new)
                .collect(Collectors.toList());

        return BoardListResponseDTO.builder()
                .boards(dtoList)
                .build();
    }

    //board 삭제 처리
    public BoardListResponseDTO delete(Long boardNo){

        try{
            boardRepository.deleteById(boardNo);
        }catch (Exception e){
            log.error("board의 번호가 존재하지 않아서 삭제에 실패 했습니다. -{}, error : {}",
                    boardNo, e.getMessage());
            throw new RuntimeException("삭제에 실패 하셨습니다.");
        }
        return retrieve();
    }

}
