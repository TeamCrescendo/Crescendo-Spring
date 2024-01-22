package com.crescendo.board.service;

import com.crescendo.blackList.repository.BlackListRepository;
import com.crescendo.board.dto.request.BoardModifyRequestDTO;
import com.crescendo.board.dto.request.BoardRequestDTO;
import com.crescendo.board.dto.response.BoardListResponseDTO;
import com.crescendo.board.dto.response.BoardResponseDTO;
import com.crescendo.board.entity.Board;
import com.crescendo.board.entity.Dislike;
import com.crescendo.board.entity.Like;
import com.crescendo.member.entity.Member;
import com.crescendo.board.repository.BoardRepository;
import com.crescendo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional // !! JPA 사용시 필수!!
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BlackListRepository blackListRepository;


    //board에 등록
    public BoardListResponseDTO create(BoardRequestDTO dto) {
        Member member = memberRepository.getOne(dto.getAccount());
        if (member == null) {
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

    //board 수정
    public boolean modifyBoard(BoardModifyRequestDTO dto) {
        Board board = boardRepository.getOne(dto.getBoardNo());
        if (!board.getMember().getAccount().equals(dto.getAccount())) {
            return false;
        }
        board.setBoardTitle(dto.getBoardTitle());
        board.setScoreImgUrl(dto.getScoreImgUrl());

        return true;
    }

    //board 삭제 처리
    public BoardListResponseDTO delete(Long boardNo) {

        try {
            boardRepository.deleteById(boardNo);
        } catch (Exception e) {
            log.error("board의 번호가 존재하지 않아서 삭제에 실패 했습니다. -{}, error : {}",
                    boardNo, e.getMessage());
            throw new RuntimeException("삭제에 실패 하셨습니다.");
        }
        return retrieve();
    }


    //좋아요와 싫어요 기능 처리
    public boolean boardLikeAndDislike(Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);

        //좋아요를 누르기 구현
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            //좋아요를 누른 상태라면
            if(board.getBoardLike() == Like.LIKE){
                //좋아요를 취소하고
                board.setBoardLike(Like.UNLIKE);
                //좋아요를 누른 상태에서 싫어요를 누르면
            } else if (board.getBoardDislike() == Dislike.DISLIKE) {
                //좋아요를 취고하고
                board.setBoardLike(Like.UNLIKE);
                //싫어요를 누르기
                board.setBoardDislike(Dislike.DISLIKE);
                //좋아요가 안눌러진 상태라면
            }else {
                board.setBoardLike(Like.LIKE);
            }
            return true;
        }

        //싫어요를 누르기 구현
        if(optionalBoard.isPresent()){
            Board board1 = optionalBoard.get();
            //싫어요를 누른 상태라면
            if(board1.getBoardDislike() == Dislike.DISLIKE){
                //싫어요를 취소하기
                board1.setBoardDislike(Dislike.UNDISLIKE);
                //싫어요를 누른 상태에서 좋아요를 누르면
            } else if (board1.getBoardLike() == Like.LIKE) {
                //싫어요를 취소하고 좋아요를 누르기
                board1.setBoardDislike(Dislike.UNDISLIKE);
                board1.setBoardLike(Like.LIKE);
                //싫어요가 안눌러진 상태라면
            } else if (board1.getBoardDislike().getValue() >= 5) {

            } else {
                board1.setBoardDislike(Dislike.DISLIKE);
            }
            return true;
        }
        return false;
    }

}
