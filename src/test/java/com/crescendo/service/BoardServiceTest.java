//package com.crescendo.service;
//
//import com.crescendo.dto.request.BoardRequestDTO;
//import com.crescendo.entity.Board;
//import com.crescendo.entity.Member;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@SpringBootTest
//@Transactional
//@Rollback(value = false)
//class BoardServiceTest {
//
//        @Autowired
//    BoardService boardService;
//
//        @Test
//        @DisplayName("게시글 만들기에 성공을 해야 한다.")
//        void createBoard() {
//            //given
//            BoardRequestDTO account= BoardRequestDTO.builder()
//                    .boardTitle("Test board~~")
//                    .account(Member.builder()
//                            .account("hhhhgong6")
//                            .build())
//                    .build();
//            //when
//            boardService.create(account);
//            //then
//        }
//
//
//}