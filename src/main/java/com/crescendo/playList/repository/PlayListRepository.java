package com.crescendo.playList.repository;

import com.crescendo.allPlayList.entity.AllPlayList;
import com.crescendo.board.entity.Board;
import com.crescendo.playList.dto.responseDTO.PlayListResponseDTO;
import com.crescendo.playList.entity.PlayList;
import com.crescendo.score.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlayListRepository extends JpaRepository<PlayList, Long > {


   List<PlayList> findByPlId(AllPlayList allPlayList);

   List<PlayList> findByPlIdAndScore (Long plId, int scoreNo);



//   @Query("SELECT new com.crescendo.board.dto.response.MyBoardListResponseDTO(" +
//           "b.boardNo, b.boardTitle, b.boardLikeCount, b.boardDislikeCount, " +
//           "b.boardViewCount, b.boardDownloadCount) " +
//           "FROM Board b")
//   List<MyBoardListResponseDTO> findMyBoardResponseDTO(String account);

   @Query("SELECT new com.crescendo.playList.dto.responseDTO.PlayListResponseDTO(" +
           "p.plNo, p.plAddDateTime, p.plId.plId, p.board.boardTitle) " +
           "FROM PlayList p WHERE p.plId.account.account = :account AND p.plId.plId = :plId")
   List<PlayListResponseDTO> findByPlNoAndAndPlAddDateTimeAndPlIdAndScore (@Param("account") String account, @Param("plId") Long plId);

   @Transactional
   @Modifying
   @Query("DELETE FROM PlayList p WHERE p.plId.account.account = :account AND p.plNo = :plNo")
   void deleteByAccountAndPlNo(@Param("account") String account, @Param("plNo") Long plNo);

   PlayList findByScore(int scoreNo);

   boolean existsByPlIdAndScore(AllPlayList allPlayList, Score score);

   List<PlayList> findByBoard(Board board);
}
