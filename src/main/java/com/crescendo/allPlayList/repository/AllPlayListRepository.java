package com.crescendo.allPlayList.repository;

import com.crescendo.allPlayList.entity.AllPlayList;
import com.crescendo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AllPlayListRepository extends JpaRepository<AllPlayList , Long> {

    //플레이 리스트 제목으로 검색하기
    @Query("SELECT title from  AllPlayList title WHERE title.plName LIKE %:title%")
    AllPlayList searchByTitleWithJPQL(@Param("title") String name);


    //플레이 리스트 만든 계정으로 조회하기
    List<AllPlayList> findAllByAccount(String member);

    //플레이 리스트 내가 만든거 삭제하기
    @Modifying //SELECT가 아닌경우 무조건 붙히기
    @Query("DELETE FROM AllPlayList  allplaylist WHERE allplaylist.account=?1 AND allplaylist.plId = ?2")
    void deleteByAccountWithJPQL(Member account, Long plId);
}

