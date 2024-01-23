package com.crescendo.restore.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestoreWhetherResponseDTO {
    private String restoreNo;
    private String account;
}
