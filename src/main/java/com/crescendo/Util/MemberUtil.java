package com.crescendo.Util;

import com.crescendo.member.entity.Member;
import com.crescendo.member.service.MemberService;
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
