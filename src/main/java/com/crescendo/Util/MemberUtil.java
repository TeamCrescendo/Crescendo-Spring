package com.crescendo.Util;

import com.crescendo.entity.Member;
import com.crescendo.repository.MemberRepository;
import com.crescendo.service.MemberService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberUtil {
    private  final MemberService memberService;
    public Member findMember(String account){
        Member user = memberService.findUser(account);
        if(user != null){
            return user;
        }
        return null;
    }
}
