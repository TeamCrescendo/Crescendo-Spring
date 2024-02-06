package com.crescendo.board.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardViewCountResponseDTO {

    private Long boardNo;
    private Long boardViewCount;
}
