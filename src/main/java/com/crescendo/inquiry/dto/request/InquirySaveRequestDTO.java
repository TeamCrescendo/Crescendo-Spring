package com.crescendo.inquiry.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquirySaveRequestDTO {
    @NotBlank
    private String account;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

}
