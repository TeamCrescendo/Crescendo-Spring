package com.crescendo.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInRequestDTO {
    @NotBlank
    @Size(min = 4 , max = 20)
    private String account;

    @NotBlank
    @Size(min = 4 , max = 20)
    private String password;


}
