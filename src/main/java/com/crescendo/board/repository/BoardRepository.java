package com.crescendo.board.repository;

import com.crescendo.board.dto.response.BoardResponseDTO;
import com.crescendo.board.dto.response.MyBoardListResponseDTO;
import com.crescendo.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> {


    //멤버 찾기
    List<Board> findByMember_Account(String member);

    //멤버와 게시글 같이 찾기
   Board findByMember_AccountAndAndBoardNo(String member , Long boardNo);

   //보드 찾기
    Board  findByBoardNo(Long boardNo);

//    //계정 찾기
//    Optional<Board> findByAccount(String account);


//    Optional<Board> deleteByAccount(String account);

    @Query("SELECT new com.crescendo.board.dto.response.BoardResponseDTO(" +
            "b.boardNo, b.boardTitle, b.member.account, " +
            "b.boardLikeCount, b.boardDislikeCount, b.boardViewCount, b.boardDownloadCount, " +
            "s.scoreNo, s.scoreTitle, s.scoreImageUrl, s.scoreUploadDateTime) " +
            "FROM Board b " +
            "JOIN b.scoreNo s " +
            "WHERE b.visible = true " + // isVisible이 true인 것만 조회
            "ORDER BY b.boardUpdateDateTime DESC ")
    List<BoardResponseDTO> findAllBoardResponseDTO();


    //boardNo, boardTitle, boardLikeCount, boardDislikeCount. boardViewCount, boardDownloadCount만 가져오기
    @Query("SELECT new com.crescendo.board.dto.response.MyBoardListResponseDTO(" +
            "b.boardNo, b.boardTitle, b.boardLikeCount, b.boardDislikeCount, " +
            "b.boardViewCount, b.boardDownloadCount) " +
            "FROM Board b")
    List<MyBoardListResponseDTO> findMyBoardResponseDTO(String account);



    //board의 조회수 찾기
    Board findBoardByBoardNoAndBoardViewCount(Long boardNo, Long boardViewCount );

    // 페이징 처리 된 보드 찾기
    Page<Board> findAll(Pageable pageable);

    Page<Board> findAllByVisibleTrue(Pageable pageable);


}

