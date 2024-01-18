package com.crescendo.repository;

import com.crescendo.entity.Member;
import com.crescendo.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Integer> {
    List<Score> findAllByMember(Member member);
}