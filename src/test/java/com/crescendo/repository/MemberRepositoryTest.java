package com.crescendo.repository;

import com.crescendo.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback()
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void insertBeforeTest() {
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
    }


    @Test
    @DisplayName("멤버를 만들어서 값을 넣으면 새로 생성된 member는 notNull이다.")
    void saveTest() {
        //given
        Member member1 = Member.builder()
                .account("member1")
                .email("member1@naver.com")
                .password("1234")
                .userName("회원1")
                .build();
        //when
        Member savedMember = memberRepository.save(member1);
        //then
        assertNotNull(savedMember);
        System.out.println("\n\n\n");
        System.out.println("savedMember = " + savedMember.toString());
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("단일 조회 테스트 account가 member1인 사람을 찾는다")
    void getOneTest() {
        //given
        String account = "member1";
        //when
        Member foundMember = memberRepository.getOne(account);
        //then
        assertNotNull(foundMember);
        System.out.println("\n\n\n");
        System.out.println("foundMember = " + foundMember.toString());
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("유저 아이디랑 이메일 유저이름 중복 검사")
    void duplicationTest() {
        //given
        String type = "account";
        String keyword = "member6";

        String type1 = "email";
        String keyword1 = "member1@naver.com";

        String type2 = "user_name";
        String keyword2 = "회원2";
        //when
        int duplication = memberRepository.isDuplication(type, keyword);
        int duplication1 = memberRepository.isDuplication(type1, keyword1);
        int duplication2 = memberRepository.isDuplication(type2, keyword2);
        //then
        assertEquals(0, duplication);
        assertEquals(1, duplication1);
        assertEquals(1, duplication1);
    }

    @Test
    @DisplayName("전체 조회를 하면 3명이 조회된다")
    void findAllTest() {
        //given

        //when
        List<Member> all = memberRepository.findAll();
        //then
        assertEquals(3, all.size());
        System.out.println("\n\n\n");
        System.out.println("all = " + all);
        System.out.println("\n\n\n");
    }

}