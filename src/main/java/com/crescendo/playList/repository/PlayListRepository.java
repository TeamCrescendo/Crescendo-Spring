package com.crescendo.playList.repository;

import com.crescendo.playList.entity.PlayList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayListRepository extends JpaRepository<PlayList, Long > {


}
