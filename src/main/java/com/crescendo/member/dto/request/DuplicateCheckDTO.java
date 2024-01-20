package com.crescendo.member.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DuplicateCheckDTO {
    @NotBlank
    String target;
    @NotBlank
    String value;
}
