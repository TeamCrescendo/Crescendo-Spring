package com.crescendo.member.entity;

import com.crescendo.allPlayList.entity.AllPlayList;
import com.crescendo.allPlayList.repository.AllPlayListRepository;
import com.crescendo.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = true)
class MemberJoinTest {

    @Autowired
    AllPlayListRepository allPlayListRepository;

    @Autowired
    MemberRepository memberRepository;
    @Test
    void  insetAllplayList(){
        //멤버가 삭제되면 allplayList도 같이 삭제되야함
        Member member1 = memberRepository.getOne("member1");
//        memberRepository.delete(member1);
//        List<AllPlayList> member1LIst = allPlayListRepository.findAllByAccount("member1");
//        assertEquals(0,member1LIst.size());


    }

}