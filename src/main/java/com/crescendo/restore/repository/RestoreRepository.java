package com.crescendo.restore.repository;

import com.crescendo.member.entity.Member;
import com.crescendo.restore.entity.Restore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestoreRepository extends JpaRepository<Restore, String> {

    // 현재 시간 보다 작으면 ID 가져오는 쿼리
    @Query("SELECT r FROM Restore r WHERE r.deleteTime <= CURRENT_TIMESTAMP")
    List<Restore> getRestoreByOverTime();

    boolean existsByMemberAccount(String account);

    Restore findByMemberAccount(String account);

    boolean deleteByMember(Member member);

}
