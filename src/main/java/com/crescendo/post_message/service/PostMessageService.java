package com.crescendo.post_message.service;

import com.crescendo.member.entity.Member;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.post_message.dto.request.SendMessageRequestDTO;
import com.crescendo.post_message.entity.PostMessage;
import com.crescendo.post_message.repository.PostMessageRepository;
import com.crescendo.score.exception.NoArgumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class PostMessageService {
    private final PostMessageRepository postMessageRepository;
    private final MemberRepository memberRepository;

    // 쪽지 보내기
    public boolean sendPostMessage(SendMessageRequestDTO dto){
        // dto가 널일 때
        if(dto==null){
            throw new NoArgumentException("제대로 된 값을 보내주세요.");
        }

        // 보내는 사람이 없을 때 DB에
        Member foundMember = memberRepository.getOne(dto.getAccount());
        if (foundMember == null){
            throw new NoMatchAccountException("보내는 사람을 다시 확인해주세요!");
        }

        Member receiveMember = memberRepository.getOne(dto.getReceiver());
        if(receiveMember == null){
            throw new NoMatchAccountException("받는 사람을 다시 확인해주세요!");
        }

        PostMessage build = PostMessage.builder()
                .member(foundMember)
                .postMessageContent(dto.getContent())
                .postMessageReceiver(dto.getReceiver())
                .build();

        postMessageRepository.save(build);
        return true;



    }

    // 받은 쪽지 리스트
    public boolean findMessageAll(String receiver){
        boolean b = memberRepository.existsByAccount(receiver);
        if(!b){
            throw new NoMatchAccountException("정확한 계정명을 보내주세요!");
        }

        List<PostMessage> allByPostMessageReceiver = postMessageRepository.findAllByPostMessageReceiver(receiver);
        allByPostMessageReceiver.forEach(postMessage -> {
            
        });

        return false;
    }

}
