package com.crescendo.score.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindByAccountScoreResponseDTO {
    private String scoreTitle; // 악보 제목
    private String account; // 악보 작성자
    private String scoreImageUrl; // 악보 표지
    private String scoreGenre; // 악보 장르
    private int scoreId; // 악보 아이디



}
