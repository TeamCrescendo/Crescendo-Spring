package com.crescendo.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardModifyRequestDTO {

    private Long boardNo;
    private String account;
    private String boardTitle;
    private String scoreImgUrl;

}
