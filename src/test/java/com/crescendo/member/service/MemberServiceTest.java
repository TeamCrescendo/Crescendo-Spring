package com.crescendo.member.service;

import com.crescendo.allPlayList.repository.AllPlayListRepository;
import com.crescendo.member.entity.Member;
import com.crescendo.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AllPlayListRepository allPlayListRepository;

    @Test
    @DisplayName("account를 넣으면 count값이 나온다")
    void downcountbyaccountTest() {
        String account="member1";
        int i = memberRepository.downCountUserDownloadChanceByAccount(account);
        System.out.println("i = " + i);

    }


    @Test
    @DisplayName("member1을 지우면 member1과 연관된 모든 allplayList와 playList가 전부 삭제된다.")
    void deleteMember() {
        String account="member1";
        //allplayList지우면 각각의 playList도 전부 삭제되게 성공
        Member one = memberRepository.getOne(account);
        allPlayListRepository.deleteByAccount(one);



    }





}