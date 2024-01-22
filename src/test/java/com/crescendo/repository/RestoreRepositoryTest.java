package com.crescendo.repository;

import com.crescendo.member.entity.Member;
import com.crescendo.restore.entity.Restore;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.restore.repository.RestoreRepository;
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
@Rollback
class RestoreRepositoryTest {
    @Autowired
    RestoreRepository restoreRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void bulkInsert() {
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

        Restore restore = Restore.builder().member(member1).build();


        restoreRepository.save(restore);
    }

    @Test
    @DisplayName("Join Test")
    void joinTest() {
        //given
        String account = "member1";
        //when
        Optional<Member> member = memberRepository.findById(account);


        member.ifPresent(System.out::println);

        Restore build = Restore.builder()
                .member(member.get())
                .build();
        //then
        assertNotNull(build);
        System.out.println("build = " + build);
    }


    @Test
    @DisplayName("Restore에 세이브 테스트")
    void saveTest() {
        //given
        String account = "member1";

        Optional<Member> member = memberRepository.findById(account);


        member.ifPresent(System.out::println);

        Restore build = Restore.builder()
                .member(member.get())
                .build();
        //when
        Restore save = restoreRepository.save(build);
        //then
        assertNotNull(save);
        System.out.println("\n\n\n");
        System.out.println("save = " + save);
        System.out.println("\n\n\n");

    }
}