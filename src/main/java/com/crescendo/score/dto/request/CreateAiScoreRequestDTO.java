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
public class CreateAiScoreRequestDTO {

    private String account;
    private String prompt;
    private int duration;
}

