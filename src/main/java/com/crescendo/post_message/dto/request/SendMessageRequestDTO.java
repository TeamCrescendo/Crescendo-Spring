package com.crescendo.post_message.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMessageRequestDTO {
    @NotBlank
    private String account; // 보내는 사람
    @NotBlank
    private String content; // 내용
    @NotBlank
    private String receiver; // 받는 사람
}
