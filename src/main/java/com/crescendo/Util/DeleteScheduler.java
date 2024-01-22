package com.crescendo.Util;

import com.crescendo.inquiry.entity.Inquiry;
import com.crescendo.inquiry.repository.InquiryRepository;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.restore.entity.Restore;
import com.crescendo.restore.repository.RestoreRepository;
import com.crescendo.score.entity.Score;
import com.crescendo.score.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteScheduler {
    private final RestoreRepository restoreRepository;
    private final MemberRepository memberRepository;
    private final InquiryRepository inquiryRepository;
    private final ScoreRepository scoreRepository;
    private final EntityManager entityManager;

    @Scheduled(fixedRate = 1000 * 60 * 1) // 1분 마다
    @Transactional
    public void performDeleteTask() {
        List<Restore> restoreByOverTime = restoreRepository.getRestoreByOverTime();
        if (restoreByOverTime != null) {
            for (Restore restore : restoreByOverTime) {
                String account = restore.getMember().getAccount();
                changeInquiryMemberToNull(account);
                changeScoreMemberToNull(account);
                restoreRepository.deleteById(restore.getRestoreNo());
                memberRepository.deleteById(account);
            }
        }

    }

    // 문의 member null 로 바꾸기
    @Transactional
    public void changeInquiryMemberToNull(String account) {
        List<Inquiry> allByMemberAccountOrderByInquiryDateTimeDesc = inquiryRepository.findAllByMemberAccountOrderByInquiryDateTimeDesc(account);
        allByMemberAccountOrderByInquiryDateTimeDesc.forEach(inquiry -> {
            inquiry.setMember(null);
            entityManager.merge(inquiry);
        });
    }

    // 악보 member null 로 바꾸기
    @Transactional
    public void changeScoreMemberToNull(String account){
        List<Score> allByMemberAccount = scoreRepository.findAllByMemberAccount(account);
        allByMemberAccount.forEach(score -> {
            score.setMember(null);
        });
    }
}
