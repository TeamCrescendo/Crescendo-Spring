package com.crescendo.repository;

import com.crescendo.entity.AllPlayList;
import com.crescendo.entity.Member;
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
class AllPlayListRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AllPlayListRepository allPlayListRepository;

    @Test
    @DisplayName("AllPlayList에 재생 목록을 하나 추가한다.")
    void AllPlayListSaveTest() {
        //given
        Member member4 = Member.builder()
                .account("member4")
                .email("member4@naver.com")
                .password("1234")
                .userName("회원4")
                .build();
        memberRepository.save(member4);
        //when
        AllPlayList build = AllPlayList.builder()
                .account(member4)
                .plName("TestPlayList")
                .share(false)
                .build();
        //then
        allPlayListRepository.save(build);
    }




}