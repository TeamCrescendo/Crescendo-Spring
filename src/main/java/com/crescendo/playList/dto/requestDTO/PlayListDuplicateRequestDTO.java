package com.crescendo.playList.dto.requestDTO;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayListDuplicateRequestDTO {
    private List<PlayListRequestDTO> list;
}
