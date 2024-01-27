package com.crescendo.score.repository;

import com.crescendo.member.entity.Member;
import com.crescendo.score.entity.Score;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface ScoreRepository extends JpaRepository<Score, Integer> {
    List<Score> findAllByMember(Member member);

    List<Score> findAllByMemberAccount(String account);

    Score findByScoreNo(int score);

    Score findByScoreImageUrl(String url);
}
