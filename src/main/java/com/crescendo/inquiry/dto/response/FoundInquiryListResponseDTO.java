package com.crescendo.inquiry.dto.response;

import com.crescendo.inquiry.entity.Inquiry;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private boolean check;
    private LocalDateTime createTime;

    public FoundInquiryListResponseDTO(Inquiry inquiry) {
        this.inquiryTitle = inquiry.getInquiryTitle();
        this.inquiryContent = inquiry.getInquiryContent();
        this.inquiryId = inquiry.getInquiryId();
        this.account = inquiry.getMember().getAccount();
        this.createTime = inquiry.getInquiryDateTime();
        this.check = inquiry.isCheck();
    }
}
