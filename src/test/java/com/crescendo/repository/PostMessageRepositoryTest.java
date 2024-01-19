package com.crescendo.repository;

import com.crescendo.member.entity.Member;
import com.crescendo.entity.PostMessage;
import com.crescendo.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
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
class PostMessageRepositoryTest {
    @Autowired
    PostMessageRepository postMessageRepository;

    @Autowired
    MemberRepository memberRepository;
    @BeforeEach
    void bulkInsert(){
        Member one = memberRepository.getOne("member2");

        PostMessage postMessage = PostMessage.builder().postMessageReceiver("member3").member(one).postMessageContent("ㅎㅇ").build();
        postMessageRepository.save(postMessage);
    }
    @Test
    @DisplayName("쪽지가 하나 있어야 한다")
    void findOneTest(){
    //given

    //when
        List<PostMessage> all = postMessageRepository.findAll();
        //then
        assertNotNull(all);
        System.out.println("\n\n\n");
        System.out.println("all = " + all);
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("쪽지를 저장할 수 있다 그러면 쪽지가 2개가 된다")
    void saveTest(){
    //given
        Member one = memberRepository.getOne("member2");

        PostMessage postMessage = PostMessage.builder()
                .postMessageContent("ㅎㅇ2")
                .postMessageReceiver("member3")
                .member(one)
                .build();
        //when
        PostMessage save = postMessageRepository.save(postMessage);
        List<PostMessage> all = postMessageRepository.findAll();
        //then
        assertEquals(2, all.size());
        System.out.println("\n\n\n");
        System.out.println("all.toString() = " + all.toString());
        System.out.println("\n\n\n");
    }
}