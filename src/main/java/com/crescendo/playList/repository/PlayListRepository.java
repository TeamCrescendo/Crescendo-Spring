package com.crescendo.playList.repository;

import com.crescendo.allPlayList.entity.AllPlayList;
import com.crescendo.playList.entity.PlayList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayListRepository extends JpaRepository<PlayList, Long > {


   List<PlayList> findByPlId(AllPlayList allPlayList);

   PlayList findByPlIdAndScore(Long plId, int score);
}
