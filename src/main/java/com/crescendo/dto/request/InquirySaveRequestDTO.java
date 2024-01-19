package com.crescendo.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquirySaveRequestDTO {
    private String account;
    private String title;
    private String content;

}
