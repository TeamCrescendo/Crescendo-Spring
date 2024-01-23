package com.crescendo.likeAndDislike.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeAndDislikeRequestDTO {

    private String account;
    private Long boardNo;
    private boolean like;
}
