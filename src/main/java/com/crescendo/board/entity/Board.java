package com.crescendo.board.entity;

import com.crescendo.member.entity.Member;
import com.crescendo.score.entity.Score;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_no")
    private Long boardNo;

    @Column(name = "board_title", nullable = false, length = 45)
    private String boardTitle;

    @ManyToOne()
    @JoinColumn(name = "account", nullable = false)
    private Member member;


    @Column(name = "board_likeCount", nullable = false)
    @Builder.Default
    private int boardLikeCount = 0;


    @Column(name = "board_dislikeCount", nullable = false)
    @Builder.Default
    private int boardDislikeCount = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime boardUpdateDateTime;

    @Column(name = "board_view_count")
    @Builder.Default
    private Long boardViewCount = 0L;

    @Column(name = "board_download_count")
    @Builder.Default
    private Integer boardDownloadCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "score_no", nullable = false)
    private Score scoreNo;

    @Column(name = "is_visible")
    @Builder.Default
    private boolean visible = true;
}
