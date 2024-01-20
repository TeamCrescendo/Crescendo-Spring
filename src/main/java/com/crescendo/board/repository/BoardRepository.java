package com.crescendo.board.repository;

import com.crescendo.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByMember(String member);





}
