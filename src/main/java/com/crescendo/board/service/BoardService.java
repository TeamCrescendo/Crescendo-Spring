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
    public boolean LikeAndDislike(String account, Long boardNo, Like like, Dislike dislike) {

        LikeAndDislike.builder()
                .
                .build();

        //게시글을 가져오기
        Optional<Board> boardId = boardRepository.findById(boardNo);

        if (boardId.isPresent()) {
            Board board = boardId.get();

            //사용자를 가져오기
            Member member = memberRepository.getOne(account);

            if (member == null) {
                log.error("사용자가 존재하지 않아요: {}", account);
                return false;
            }
        }

            //이미 좋아요를 눌렀는지 확인
            //만약 좋아요가 비어있지 않고, 내가 좋아요를 누른 상태에서 좋아요를 누르면 좋아요 취소
            if (like != null && board.getBoardLike() == Like.LIKE) {
                log.info("이미 좋아요를 누른 게시글 입니다.");
                board.setBoardLike(Like.UNLIKE);
                board.setBoardDislike(Dislike.DISLIKE);
                return true; //좋아요 취소를 성공적으로 반환 했음
            }
            //이미 싫어요를 눌렀는지 확인
            //만약 싫어요가 비어있지 않고, 내가 싫어요를 누른 상태에서 싫어요를 누르면 싫어요 취소
            if(dislike != null && board.getBoardDislike() == Dislike.DISLIKE){
                log.info("이미 싫어요를 누른 게시글 입니다.");
                board.setBoardDislike(Dislike.UNDISLIKE);
                board.setBoardLike(Like.LIKE);
                return true; //싫어요 취소를 성공적으로 반환 했음
            }

            //만약 좋아요가 눌러있지 않은 상태라면
            //좋아요를 누르기
            if(like != null && board.getBoardLike() == Like.UNLIKE){
                log.info("좋아요를 눌렀습니다 !");
                board.setBoardLike(Like.LIKE);
                return true;
            }
            //만약 싫어요가 눌러있지 않은 상태라면
            //싫어요를 누르기
            if(dislike != null && board.getBoardDislike() == Dislike.UNDISLIKE){
                log.info("싫어요를 눌렀습니다!");
                board.setBoardDislike(Dislike.DISLIKE);
                return true;
            }
            //만약 해당 게시물의 싫어요의 수가 5가 넘어가면, 그 게시물을 blackList에 추가
            if(board.getBoardDislike()!= null && board.getBoardDislike().getValue() >= 5){
                BlackList build = BlackList.builder()
                        .board(board)
                        .member(member)
                        .build();
                try {
                    blackListRepository.save(build);
                    boardRepository.delete(board);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }


        return false;
    }
}
