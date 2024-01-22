package com.crescendo.Util;

import com.crescendo.member.repository.MemberRepository;
import com.crescendo.restore.entity.Restore;
import com.crescendo.restore.repository.RestoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteScheduler {
    private final RestoreRepository restoreRepository;
    private final MemberRepository memberRepository;
    @Scheduled(fixedRate= 1000 * 60 * 1) // 1분 마다
    public void performDeleteTask(){
        List<Restore> restoreByOverTime = restoreRepository.getRestoreByOverTime();
        for (Restore restore : restoreByOverTime) {
            String account = restore.getMember().getAccount();
            restoreRepository.deleteById(restore.getRestoreNo());
            memberRepository.deleteById(account);
        }


    }
}
