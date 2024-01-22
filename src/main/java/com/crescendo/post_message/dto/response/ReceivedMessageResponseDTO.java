package com.crescendo.post_message.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivedMessageResponseDTO {
    private String receiver; // 받은 사람
    private String sender; // 보낸사람
    private String content; // 내용
    private boolean check; // 확인 여부
}
