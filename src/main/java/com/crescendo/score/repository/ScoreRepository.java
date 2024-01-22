package com.crescendo.score.repository;

import com.crescendo.member.entity.Member;
import com.crescendo.score.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Integer> {
    List<Score> findAllByMember(Member member);

    List<Score> findAllByMemberAccount(String account);
}
