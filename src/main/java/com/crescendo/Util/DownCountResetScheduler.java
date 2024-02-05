package com.crescendo.Util;

import com.crescendo.member.entity.Member;
import com.crescendo.member.entity.Member.Auth;
import com.crescendo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DownCountResetScheduler {
    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void resetDownCount() {
        Auth userAuth = Auth.USER;
        List<Member> userMembers = memberRepository.findMemberByAuth(userAuth.toString());
        userMembers.forEach(user-> user.setUserDownloadChance(5));

    }
}
