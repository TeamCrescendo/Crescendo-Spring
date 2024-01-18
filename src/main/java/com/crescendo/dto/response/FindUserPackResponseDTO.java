package com.crescendo.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindUserPackResponseDTO {
    private FindUserResponseDTO findUser;
}
