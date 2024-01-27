package com.crescendo.post_message.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNotationRequestDTO {
    private String url;
    private String account;
}
