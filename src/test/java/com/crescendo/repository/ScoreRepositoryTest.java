package com.crescendo.repository;

import com.crescendo.member.entity.Member;
import com.crescendo.entity.Score;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.score.repository.ScoreRepository;
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
class ScoreRepositoryTest {
    @Autowired
    ScoreRepository scoreRepository;
    @Autowired
    MemberRepository memberRepository;


    @BeforeEach
    void bulkInsert(){
        Member member2 = memberRepository.getOne("member2");
        Score summer = Score.builder()
                .scoreGenre(Score.GENRE.JAZZ)
                .member(member2)
                .scoreTitle("Summer 악보에요")
                .build();
        Score save = scoreRepository.save(summer);
    }
    @Test
    @DisplayName("악보를 하나 저장하면 하나가 조회된다")
    void saveTest() {
        //given
        Member member2 = memberRepository.getOne("member2");
        Score summer = Score.builder()
                .scoreGenre(Score.GENRE.JAZZ)
                .member(member2)
                .scoreTitle("Summer 악보에요")
                .build();
        //when
        Score save = scoreRepository.save(summer);
        //then
        assertNotNull(save);
        assertEquals(1, scoreRepository.findAll().size());
        System.out.println("\n\n\n");
        System.out.println("save = " + save);
        System.out.println("\n\n\n");

    }

    @Test
    @DisplayName("사용자 이름으로 악보 찾기")
    void findAllByMemberTest() {
        //given
        String account = "member2";
        Member member2 = memberRepository.getOne(account);
        //when
        List<Score> allByMember = scoreRepository.findAllByMember(member2);
        //then
        assertNotNull(allByMember);
        System.out.println("\n\n\n");
        System.out.println("allByMember = " + allByMember);
        System.out.println("\n\n\n");
    }


}