package com.crescendo.board.repository;

import com.crescendo.board.dto.response.BoardResponseDTO;
import com.crescendo.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> {


    //멤버 찾기
    List<Board> findByMember_Account(String member);

    //멤버와 게시글 같이 찾기
   List<Board> findByMember_AccountAndAndBoardNo(String member , Long boardNo);

   //보드 찾기
    Board  findByBoardNo(Long boardNo);

//    //계정 찾기
//    Optional<Board> findByAccount(String account);


//    Optional<Board> deleteByAccount(String account);

    @Query("SELECT new com.crescendo.board.dto.response.BoardResponseDTO(" +
            "b.boardNo, b.boardTitle, b.member.account, " +
            "b.boardLikeCount,b.boardDislikeCount ,b.boardViewCount, b.boardDownloadCount, " +
            "s.scoreNo, s.scoreTitle, s.scoreImageUrl, s.scoreUploadDateTime) " +
            "FROM Board b " +
            "JOIN b.scoreNo s")
    List<BoardResponseDTO> findAllBoardResponseDTO();



    //board의 조회수 찾기
    Board findBoardByBoardNoAndBoardViewCount(Long boardNo, Long boardViewCount );




}

