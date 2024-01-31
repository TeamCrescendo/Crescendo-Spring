package com.crescendo.score.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AiMusicRequestDTO {
    String prompt;
    int duration;
}
