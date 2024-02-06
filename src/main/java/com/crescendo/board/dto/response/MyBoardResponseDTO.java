package com.crescendo.board.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyBoardResponseDTO {

    private String error;
    private List<MyBoardListResponseDTO> boards;
}
