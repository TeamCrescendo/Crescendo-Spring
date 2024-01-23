package com.crescendo.post_message.service;

import com.crescendo.member.entity.Member;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.post_message.dto.request.SendMessageRequestDTO;
import com.crescendo.post_message.dto.response.ReceivedMessageResponseDTO;
import com.crescendo.post_message.dto.response.SentMessageListResponseDTO;
import com.crescendo.post_message.entity.PostMessage;
import com.crescendo.post_message.repository.PostMessageRepository;
import com.crescendo.score.exception.NoArgumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<ReceivedMessageResponseDTO> findMessageAll(String receiver){
        boolean b = memberRepository.existsByAccount(receiver);
        if(!b){
            throw new NoMatchAccountException("정확한 계정명을 보내주세요!");
        }

        List<PostMessage> allByPostMessageReceiver = postMessageRepository.findAllByPostMessageReceiver(receiver);
        List<ReceivedMessageResponseDTO> list = new ArrayList<>();
        allByPostMessageReceiver.forEach(postMessage -> {
            ReceivedMessageResponseDTO dto = ReceivedMessageResponseDTO.builder()
                    .receiver(postMessage.getPostMessageReceiver())
                    .sender(postMessage.getMember().getAccount())
                    .content(postMessage.getPostMessageContent())
                    .check(postMessage.isChecked())
                    .messageId(postMessage.getPostMessageId())
                    .build();
            list.add(dto);
        });

        return list;
    }

    // 보낸 쪽지 리스트
    public List<SentMessageListResponseDTO> findSentMessageAll(String sender){
        boolean b = memberRepository.existsByAccount(sender);
        if(!b){
            throw new NoMatchAccountException("정확한 계정명을 보내주세요!");
        }

        List<PostMessage> allByPostMessageReceiver = postMessageRepository.findAllByMemberAccount(sender);
        List<SentMessageListResponseDTO> list = new ArrayList<>();
        allByPostMessageReceiver.forEach(postMessage -> {
            SentMessageListResponseDTO dto = SentMessageListResponseDTO.builder()
                    .receiver(postMessage.getPostMessageReceiver())
                    .sender(postMessage.getMember().getAccount())
                    .content(postMessage.getPostMessageContent())
                    .check(postMessage.isChecked())
                    .build();
            list.add(dto);
        });

        return list;
    }

    // 쪽지 확인
    public boolean checkedMessage(String messageId){
        boolean flag = postMessageRepository.existsById(messageId);
        if(!flag){
            throw new NoArgumentException("정확한 메세지 ID를 보내주세요");
        }

        Optional<PostMessage> byId = postMessageRepository.findById(messageId);
        byId.ifPresent(postMessage -> {
            postMessage.setChecked(true);
        });
        return true;
    }

}
