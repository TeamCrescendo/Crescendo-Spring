package com.crescendo.blackList.service;

import com.crescendo.blackList.entity.BlackList;
import com.crescendo.blackList.repository.BlackListRepository;
import com.crescendo.board.entity.Board;
import com.crescendo.board.repository.BoardRepository;
import com.crescendo.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BlackListService {

//    private final BoardRepository boardRepository;
//    private final BlackListRepository blackListRepository;
//
//    public void checkAndAddToBlackList(Long boardId, int dislikeThreshold) {
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//
////        if (board.getBoardDisLike() =< 5) {
////            // board의 dislike 수가 임계치 이상이면 블랙리스트에 추가하고 board에서 숨김 처리
////            addToBlackList(board.getMember());
////            hideBoard(board);
////        }
//    }
//
//    public void addToBlackList(Member member) {
//        BlackList blackList = new BlackList();
//        blackList.setMember(member);
//        blackListRepository.save(blackList);
//    }
//
//    public void hideBoard(Board board) {
//        // board를 숨김 처리하는 로직을 구현
//        // 예를 들어, board의 상태를 변경하거나 숨김 플래그를 설정하는 등의 방법으로 구현 가능
////        board.setHidden(true);
//        boardRepository.save(board);
//    }
}



