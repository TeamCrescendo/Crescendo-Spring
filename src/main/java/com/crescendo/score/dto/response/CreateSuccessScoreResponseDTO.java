package com.crescendo.score.dto.response;

import com.crescendo.score.entity.Score;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSuccessScoreResponseDTO {


    private String scoreTitle; // 악보 제목

    private String account; // 악보 작성자

    private String scoreImageUrl; // 악보 표지

    private String scoreGenre; // 악보 장르


}
