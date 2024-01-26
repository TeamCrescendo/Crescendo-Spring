package com.crescendo.report.dto.requestDTO;

import com.crescendo.board.entity.Board;
import com.crescendo.member.entity.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRepuestDTO {

    @NotBlank
    private String report_content; //신고 내용

    @NotBlank
    private Member reportReporter; //신고자

    @NotBlank
    private Board boardNo; //신고할 게시물 번호
}
