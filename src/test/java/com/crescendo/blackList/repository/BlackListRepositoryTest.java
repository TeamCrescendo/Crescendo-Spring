package com.crescendo.blackList.repository;

import com.crescendo.blackList.entity.BlackList;
import com.crescendo.board.entity.Board;
import com.crescendo.board.repository.BoardRepository;
import com.crescendo.entity.Member;
import com.crescendo.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
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
@Rollback(value = false)
class BlackListRepositoryTest {

    @Autowired
    BlackListRepository blackListRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void insertBeforeTest(){
        Member member1 = Member.builder()
                .account("member1")
                .email("member1@naver.com")
                .password("1234")
                .userName("회원1")
                .build();
        Member member2 = Member.builder()
                .account("member2")
                .email("member2@naver.com")
                .password("1234")
                .userName("회원2")
                .build();
        Member member3 = Member.builder()
                .account("member3")
                .email("member3@naver.com")
                .password("1234")
                .userName("회원3")
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Board testTitle = Board.builder()
                .boardTitle("TestTitle")
                .member(member1)
                .build();
        boardRepository.save(testTitle);

        BlackList build = BlackList.builder()
                .member(member1)
                .board(testTitle)
                .build();

        blackListRepository.save(build);
    }

    @Test
    @DisplayName("boardNo가 1인 회원을 조회 한다.")
    void findOneTest(){
        Long bno = 1L;
        BlackList blackList = blackListRepository.findById(bno).orElseThrow(
                () -> new RuntimeException("Cannot find member:"));

        System.out.println("\n\n\n");
        System.out.println("board = " + blackList);
        System.out.println("\n\n\n");
    }




}