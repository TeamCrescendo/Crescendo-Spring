package com.crescendo.likeAndDislike.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeDisLikeRequestDTO {
    private Long boardNo;
    private String account;
    private boolean like;


}
