package com.crescendo.inquiry.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoundInquiryListResponseDTO {
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryId;
    private String account;
}
