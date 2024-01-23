package com.crescendo.inquiry.dto.response;

import com.crescendo.inquiry.entity.Inquiry;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createTime;

    public FoundInquiryListResponseDTO(Inquiry inquiry) {
        this.inquiryTitle = inquiry.getInquiryTitle();
        this.inquiryContent = inquiry.getInquiryContent();
        this.inquiryId = inquiry.getInquiryId();
        this.account = inquiry.getMember().getAccount();
        this.createTime = LocalDate.from(inquiry.getInquiryDateTime());
    }
}
