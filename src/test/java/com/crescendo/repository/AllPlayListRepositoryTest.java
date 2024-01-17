package com.crescendo.repository;

import com.crescendo.entity.AllPlayList;
import com.crescendo.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class AllPlayListRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AllPlayListRepository allPlayListRepository;

    //플레이 리스트에 곡 추가하기
    @Test
    @DisplayName("AllPlayList에 재생 목록을 하나 추가한다.")
    void AllPlayListSaveTest() {
        //given
        Member member5 = Member.builder()
                .account("member6")
                .email("member6@naver.com")
                .password("1234")
                .userName("회원6")
                .build();
        memberRepository.save(member5);
        //when
        AllPlayList build = AllPlayList.builder()
                .account(member5)
                .plName("TestPlayList3")
                .share(false)
                .build();
        //then
        allPlayListRepository.save(build);
    }

    //플레이 리스트 제목으로 검색 하기
    @Test
    @DisplayName("AllPlayList에 재생 목록의 제목으로 조회 한다.")
    void findOneByPlayListTitleTest() {
        //given
        String name = "TestPlayList";
        //when
        AllPlayList allPlayList = allPlayListRepository.searchByTitleWithJPQL(name);

        Member account = allPlayList.getAccount();
        Long plId = allPlayList.getPlId();
        String plName = allPlayList.getPlName();
        LocalDateTime plCreateDateTime= allPlayList.getPlCreateDateTime();
        //then

        System.out.println("\n\n\n\n");
        System.out.println("allPlayList.getPlName() = " + allPlayList.getPlName());
        System.out.println("\n\n\n\n");
    }

    //플레이 리스트 아이디로 조회 하기
    @Test
    @DisplayName("AllPlayList에 PlayList 아이디로 조회를 한다.")
    void findOneByPlayListIdTest() {
        //given
        Long plId= 1L;
        //when
        AllPlayList allPlayList = allPlayListRepository.findById(plId).orElseThrow();
        //then
        System.out.println("\n\n\n\n");
        System.out.println("byId = " + allPlayList);
        System.out.println("\n\n\n\n");
    }

    //플레이 리스트 만든 계정으로 조회 하기
    @Test
    @DisplayName("AllPlayList에서 PlayList 계정으로 조회를 한다.")
    void findOneByPlayListAccountTest() {

        //given
        String account = "member4";
        //when
        List<AllPlayList> allByAccountAccount = allPlayListRepository.findAllByAccount_Account(account);
        //then
        System.out.println("\n\n\n\n");
        System.out.println("allByAccountAccount = " + allByAccountAccount);
        System.out.println("\n\n\n\n");
    }

    @Test
    @DisplayName("JPQL로 AllplayList삭제하기")
    void deleteWithJPQLTest() {
        //given
        Member member4 = Member.builder()
                .account("member4")
                .build();
        Long plId = 1L;
        //when
        allPlayListRepository.deleteByAccountWithJPQL(member4,plId);
        //then
    }

}