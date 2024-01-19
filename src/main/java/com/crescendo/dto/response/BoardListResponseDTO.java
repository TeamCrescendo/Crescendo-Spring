package com.crescendo.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardListResponseDTO {

    private String error;
    private List<BoardResponseDTO> boards;
}
