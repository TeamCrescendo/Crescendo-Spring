package com.crescendo.member.service;

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

    @Test
    @DisplayName("account를 넣으면 count값이 나온다")
    void downcountbyaccountTest() {
        String account="member1";
        int i = memberRepository.downCountUserDownloadChanceByAccount(account);
        System.out.println("i = " + i);

    }


}