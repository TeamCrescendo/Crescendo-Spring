package com.crescendo.playList.dto.responseDTO;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayListofListResponseDTO {

    private List<PlayListResponseDTO> playLists;


}
