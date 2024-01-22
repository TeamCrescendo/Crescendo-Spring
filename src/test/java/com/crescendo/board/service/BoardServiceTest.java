package com.crescendo.board.service;

import com.crescendo.board.entity.Board;
import com.crescendo.board.entity.Dislike;
import com.crescendo.board.entity.Like;
import com.crescendo.board.repository.BoardRepository;
import com.crescendo.likeAndDislike.dto.request.LikeAndDislikeRequestDTO;
import com.crescendo.likeAndDislike.repository.LikeAndDislikeRepository;
import com.crescendo.member.entity.Member;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback()
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("member1이 만든 게시물에 좋아요 테스트")
    void testLikeAndDislike() {
       Long boardNo = 2L;
       String account = "member2";

       boardService.LikeAndDislike(LikeAndDislikeRequestDTO.builder()
                       .boardNo(boardNo)
                       .account(account)
               .build());

    }
}