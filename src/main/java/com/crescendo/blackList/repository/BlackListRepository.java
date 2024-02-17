package com.crescendo.blackList.repository;

import com.crescendo.blackList.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlackListRepository extends JpaRepository<BlackList,Long> {

    List<BlackList> findAllByMemberAccount(String account);

}
