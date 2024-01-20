package com.crescendo.repository;

import com.crescendo.entity.Inquiry;
import com.crescendo.member.entity.Member;
import com.crescendo.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class InquiryRepositoryTest {

    @Autowired
    InquiryRepository inquiryRepository;
    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("사용자가 문의를 하면 문의에 엔터티는 널이 아니다")
    void saveTest() {
        //given
        Member one = memberRepository.getOne("member2");
        System.out.println(one.toString());
        Inquiry inquiry = Inquiry.builder()
                .inquiryTitle("씨발롬이 절 욕했다니깐")
                .inquiryContent("이거 보여줄려고 어그로 끌었다")
                .member(one)
                .build();
        //when
        Inquiry save = inquiryRepository.save(inquiry);
        //then
        assertNotNull(save);
        System.out.println("\n\n\n");
        System.out.println("save = " + save);
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("member2 계정명을 주면 그 사람이 쓴 문의를 꺼내준다")
    void getAllByAccountTest() {
        //given
        String account = "member2";
        //when
        List<String> allByAccount = inquiryRepository.getAllByAccount(account);
        //then
        assertNotNull(allByAccount);
        System.out.println("allByAccount = " + allByAccount);
    }

}