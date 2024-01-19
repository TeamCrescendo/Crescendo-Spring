package com.crescendo.blackList.dto.response;

import com.crescendo.blackList.entity.BlackList;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlackListofListResponseDTO {

    List<BlackListResponseDTO> blackLists;
}
