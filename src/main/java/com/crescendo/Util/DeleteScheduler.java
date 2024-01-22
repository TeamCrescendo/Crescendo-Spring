package com.crescendo.Util;

import com.crescendo.restore.repository.RestoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteScheduler {
    private final RestoreRepository restoreRepository;
    @Scheduled(fixedRate= 1000 * 60 * 10) // 10분 마다
    public void performDeleteTask(){
        List<String> restoreByOverTime = restoreRepository.getRestoreByOverTime();
        restoreByOverTime.forEach(restoreRepository::deleteById);
    }
}
