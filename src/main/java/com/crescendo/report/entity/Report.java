package com.crescendo.report.entity;

import com.crescendo.board.entity.Board;
import com.crescendo.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "report_no")
    private Long reportNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_reporter")
    private Member reportReporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_no")
    private Board boardNo;

    @CreationTimestamp
    @Column(name = "report_date_time")
    private LocalDateTime reportDateTime;

    @Column(name = "report_content")
    private String reportContent;
}
