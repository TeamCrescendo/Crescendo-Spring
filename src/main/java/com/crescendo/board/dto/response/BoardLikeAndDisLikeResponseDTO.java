package com.crescendo.board.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardLikeAndDisLikeResponseDTO {

    private Integer boardLikeCount;
    private Integer boardDislikeCount;

}
