package com.crescendo.Util;

import com.crescendo.inquiry.entity.Inquiry;
import com.crescendo.inquiry.repository.InquiryRepository;
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
    private final InquiryRepository inquiryRepository;

    @Scheduled(fixedRate = 1000 * 60 * 1) // 1분 마다
    public void performDeleteTask() {
        List<Restore> restoreByOverTime = restoreRepository.getRestoreByOverTime();
        if (restoreByOverTime != null) {
            for (Restore restore : restoreByOverTime) {
                String account = restore.getMember().getAccount();
                changeInquiryMemberToNull(account);
                restoreRepository.deleteById(restore.getRestoreNo());
                memberRepository.deleteById(account);
            }
        }

    }

    // 문의 member null 로 바꾸기
    private void changeInquiryMemberToNull(String account) {
        List<Inquiry> allByMemberAccountOrderByInquiryDateTimeDesc = inquiryRepository.findAllByMemberAccountOrderByInquiryDateTimeDesc(account);
        allByMemberAccountOrderByInquiryDateTimeDesc.forEach(inquiry -> {
            System.out.println("\n\n\n");
            System.out.println("inquiry = " + inquiry);
            System.out.println("\n\n\n");
            inquiry.setMember(null);
            System.out.println("inquiry = " + inquiry);
        });
    }
}
