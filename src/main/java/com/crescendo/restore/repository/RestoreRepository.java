package com.crescendo.restore.repository;

import com.crescendo.entity.Restore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RestoreRepository extends JpaRepository<Restore, String> {

    // 현재 시간 보다 작으면 ID 가져오는 쿼리
    @Query("SELECT r.restoreNo FROM Restore r WHERE r.deleteTime <= CURRENT_TIMESTAMP")
    List<String> getRestoreByOverTime();
}
