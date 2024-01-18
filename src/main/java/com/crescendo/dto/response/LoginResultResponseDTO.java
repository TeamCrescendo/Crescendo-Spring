package com.crescendo.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResultResponseDTO {
    boolean result;
    LoginUserResponseDTO dto;

}
