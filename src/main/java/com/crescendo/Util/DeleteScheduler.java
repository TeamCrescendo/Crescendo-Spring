package com.crescendo.Util;

import com.crescendo.entity.Restore;
import com.crescendo.repository.RestoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteScheduler {
    private final RestoreRepository restoreRepository;
    @Scheduled(fixedRate= 1000 * 10) // 10초 마다
    public void performDeleteTask(){
        List<String> restoreByOverTime = restoreRepository.getRestoreByOverTime();
        restoreByOverTime.forEach(restoreRepository::deleteById);
    }
}
