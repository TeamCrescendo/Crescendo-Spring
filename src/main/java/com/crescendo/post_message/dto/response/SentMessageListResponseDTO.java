package com.crescendo.post_message.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentMessageListResponseDTO {
    private String receiver; // 받은 사람
    private String sender; // 보낸사람
    private String content; // 내용
    private boolean check; // 확인 여부
    private LocalDateTime writtenTime;

    public SentMessageListResponseDTO(ReceivedMessageResponseDTO dto) {
        this.receiver = dto.getReceiver();
        this.content = dto.getContent();
        this.sender = dto.getSender();
        this.check = dto.isCheck();
        this.writtenTime = dto.getLocalDate();
    }
}
