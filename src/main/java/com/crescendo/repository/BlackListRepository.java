package com.crescendo.repository;

import com.crescendo.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlackListRepository extends JpaRepository<BlackList,Long> {
//    List<BlackList> findBlackListByBlackDisCount(Long boardNo, Long boardDislike);


}
