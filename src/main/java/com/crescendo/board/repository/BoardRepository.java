package com.crescendo.board.repository;

import com.crescendo.board.entity.Board;
import com.crescendo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long> {


    //멤버 찾기
    List<Board> findByMember_Account(String member);

    //멤버와 게시글 같이 찾기
   List<Board> findByMemberAndBoardNo(Member member , Long boardNo);

   //보드 찾기
    Board  findByBoardNo(Long boardNo);

//    //계정 찾기
//    Optional<Board> findByAccount(String account);


//    Optional<Board> deleteByAccount(String account);
}
