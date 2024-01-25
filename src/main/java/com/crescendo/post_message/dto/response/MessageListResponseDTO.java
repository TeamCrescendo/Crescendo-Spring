package com.crescendo.post_message.dto.response;

import com.crescendo.post_message.entity.PostMessage;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageListResponseDTO {
    private String receiver; // 받은 사람
    private String receiverNickname; // 받은 사람 닉네임
    private String sender; // 보낸사람
    private String senderNickName; // 받은 사람 닉네임
    private String content; // 내용
    private boolean check; // 확인 여부
    private LocalDateTime writtenTime; // 작성 시간
    private String messageId; // 메세지 아이디

    public MessageListResponseDTO(PostMessage postMessage) {

    }
}
