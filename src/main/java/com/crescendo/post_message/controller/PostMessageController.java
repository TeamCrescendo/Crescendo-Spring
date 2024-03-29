package com.crescendo.post_message.controller;

import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.util.TokenUserInfo;
import com.crescendo.post_message.dto.request.SendMessageRequestDTO;
import com.crescendo.post_message.dto.response.MessageListResponseDTO;
import com.crescendo.post_message.dto.response.ReceivedMessageResponseDTO;
import com.crescendo.post_message.dto.response.SentMessageListResponseDTO;
import com.crescendo.post_message.service.PostMessageService;
import com.crescendo.score.exception.NoArgumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class PostMessageController {
    private final PostMessageService postMessageService;

    // 쪽지 보내기
    @PostMapping
    public ResponseEntity<?> sendMessage(@Validated @RequestBody SendMessageRequestDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.toString());
        }
        try {
            boolean b = postMessageService.sendPostMessage(dto);
            return ResponseEntity.ok().body(b);
        } catch (NoArgumentException | NoMatchAccountException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 받고, 보낸 쪽지 보기
    @GetMapping
    public ResponseEntity<?> getMessages(String target, String value) {
        if (target.isEmpty()) {
            return ResponseEntity.badRequest().body("타겟을 정확히 주세요");
        }

        if (value.isEmpty()) {
            return ResponseEntity.badRequest().body("계정명 제대로 주세요");
        }

        if (target.equals("receiver")) {
            try {
                List<ReceivedMessageResponseDTO> messageAll = postMessageService.findMessageAll(value);
                return ResponseEntity.ok().body(messageAll);
            } catch (NoMatchAccountException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else if (target.equals("sender")) {
            try {
                List<SentMessageListResponseDTO> sentMessageAll = postMessageService.findSentMessageAll(value);
                return ResponseEntity.ok().body(sentMessageAll);
            } catch (NoMatchAccountException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("값을 정확히 주세요");
        }


    }

    // 쪽지 ID로 쪽지 체크 여부 false -> true
    @RequestMapping(method = {PUT, PATCH})
    public ResponseEntity<?> checkMessage(String messageId) {
        if (messageId.isEmpty()) {
            return ResponseEntity.badRequest().body("타겟을 정확히 주세요");
        }
        try {
            boolean b = postMessageService.checkedMessage(messageId);
            return ResponseEntity.ok().body(b);
        } catch (NoArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 쪽지 계정명으로 전체 조회
    @GetMapping("/all")
    public ResponseEntity<?> getMessageAll(@AuthenticationPrincipal TokenUserInfo userInfo) {
        List<MessageListResponseDTO> messageListResponseDTOS = postMessageService.messageAll(userInfo.getAccount());
        return ResponseEntity.ok().body(messageListResponseDTOS);
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostMessage(@AuthenticationPrincipal TokenUserInfo userInfo, @PathVariable String id) {
        /*
        1. 토큰 -> 유저정보
        2. 쪽지 아이디
        삭제후 포스트 메시지 리스트 다시 리턴하기
         */
        try {
            List<MessageListResponseDTO> dtoList = postMessageService.delete(userInfo.getAccount(), id);
            return ResponseEntity.ok().body(dtoList);

        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());

        }

    }
}
