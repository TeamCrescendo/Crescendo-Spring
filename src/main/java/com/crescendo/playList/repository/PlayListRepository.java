package com.crescendo.playList.repository;

import com.crescendo.allPlayList.entity.AllPlayList;
import com.crescendo.board.dto.response.MyBoardListResponseDTO;
import com.crescendo.playList.dto.responseDTO.PlayListResponseDTO;
import com.crescendo.playList.entity.PlayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlayListRepository extends JpaRepository<PlayList, Long > {


   List<PlayList> findByPlId(AllPlayList allPlayList);

   PlayList findByPlIdAndScore(Long plId, int scoreNo);

//   @Query("SELECT new com.crescendo.board.dto.response.MyBoardListResponseDTO(" +
//           "b.boardNo, b.boardTitle, b.boardLikeCount, b.boardDislikeCount, " +
//           "b.boardViewCount, b.boardDownloadCount) " +
//           "FROM Board b")
//   List<MyBoardListResponseDTO> findMyBoardResponseDTO(String account);

   @Query("SELECT new com.crescendo.playList.dto.responseDTO.PlayListResponseDTO(" +
           "p.plNo, p.plAddDateTime, p.plId.plId, p.score.scoreTitle) " +
           "FROM PlayList p WHERE p.plId.account.account = :account")
   List<PlayListResponseDTO> findByPlNoAndAndPlAddDateTimeAndPlIdAAndScore (@Param("account") String account);

   @Transactional
   @Modifying
   @Query("DELETE FROM PlayList p WHERE p.plId.account.account = :account AND p.plNo = :plNo")
   void deleteByAccountAndPlNo(@Param("account") String account, @Param("plNo") Long plNo);

}
