package com.crescendo.allPlayList.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties("member")
public class AllPlayResponseDTO {

    private List<AllPlayListResponseDTO> allPlayLists;
    }

