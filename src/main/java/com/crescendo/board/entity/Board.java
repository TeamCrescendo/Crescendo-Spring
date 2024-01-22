package com.crescendo.board.entity;

import com.crescendo.member.entity.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_like")
    private Like boardLike;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "board_dislike")
    private Dislike boardDislike;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime boardUpdateDateTime;

    @Column(name = "board_view_count")
    @Builder.Default
    private Long boardViewCount = 0L;

    @Column(name = "board_download_count")
    @Builder.Default
    private Integer boardDownloadCount = 5;

    @Column(name = "score_img_url")
    private String scoreImgUrl;
}
