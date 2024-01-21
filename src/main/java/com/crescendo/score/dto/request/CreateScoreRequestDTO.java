package com.crescendo.score.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateScoreRequestDTO {

    @NotBlank
    // @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "유저 이름은 한국어 또는 영어만 허용됩니다.")
    private String scoreTitle; // 악보 제목

    @NotBlank
    private String account; // 악보 작성자

    private String scoreImageUrl; // 악보 표지

    private String scoreGenre; // 악보 장르


}
