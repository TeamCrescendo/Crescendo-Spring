package com.crescendo.board.service;

import com.crescendo.blackList.entity.BlackList;
import com.crescendo.blackList.repository.BlackListRepository;
import com.crescendo.board.dto.request.BoardModifyRequestDTO;
import com.crescendo.board.dto.request.BoardRequestDTO;
import com.crescendo.board.dto.response.BoardListResponseDTO;
import com.crescendo.board.dto.response.BoardResponseDTO;
import com.crescendo.board.entity.Board;
import com.crescendo.board.entity.Dislike;
import com.crescendo.board.entity.Like;
import com.crescendo.likeAndDislike.dto.request.LikeDisLikeRequestDTO;
import com.crescendo.likeAndDislike.entity.LikeAndDislike;
import com.crescendo.likeAndDislike.repository.LikeAndDislikeRepository;
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
    private final LikeAndDislikeRepository likeAndDislikeRepository;


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
    public void LikeAndDislike(LikeDisLikeRequestDTO dto) {

        Long boardNo = dto.getBoardNo();
        Optional<Board> boardId = boardRepository.findById(boardNo);
        boardId.ifPresent(board -> {
            Member member = memberRepository.getOne(dto.getAccount());

            LikeAndDislike memberAccountAndBoardNo = likeAndDislikeRepository.findByMemberAccountAndBoard_BoardNo(dto.getAccount(), dto.getBoardNo());

            if (memberAccountAndBoardNo == null) {
                LikeAndDislike build = LikeAndDislike.builder()
                        .boardLike(dto.isLike())
                        .member(member)
                        .board(board)
                        .build();
                likeAndDislikeRepository.save(build);
                if (dto.isLike()) {
                    board.setBoardLikeCount(board.getBoardLikeCount() + 1);
                } else {
                    board.setBoardDislikeCount(board.getBoardDislikeCount() + 1);
                }
            } else {
                if (dto.isLike()) {
                    if (!memberAccountAndBoardNo.isBoardLike()) {
                        board.setBoardLikeCount(board.getBoardLikeCount() + 1);
                        board.setBoardDislikeCount(board.getBoardDislikeCount() - 1);
                    }
                } else {
                    if (memberAccountAndBoardNo.isBoardLike()) {
                        board.setBoardLikeCount(board.getBoardLikeCount() - 1);
                        board.setBoardDislikeCount(board.getBoardDislikeCount() + 1);
                    }
                }
            }
        });
    }
}
