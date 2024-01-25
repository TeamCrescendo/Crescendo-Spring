package com.crescendo.report.dto.responseDTO;

import com.crescendo.board.entity.Board;
import com.crescendo.member.entity.Member;
import com.crescendo.report.entity.Report;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponseDTO {

    private Long reportNo;

    private String reportContent;

    private LocalDateTime reportDateTime;

    private Board boardNo;

    private Member reportReporter;

    public ReportResponseDTO(Report report){
        this.reportNo = report.getReportNo();
        this.reportContent = report.getReportContent();
        this.reportDateTime = report.getReportDateTime();
        this.boardNo = report.getBoardNo();
        this.reportReporter = report.getReportReporter();
    }

}
