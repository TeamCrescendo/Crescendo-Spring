package com.crescendo.entity;

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

    @Column(name = "board_title",nullable = false, length = 45)
    private String boardTitle;

    // Member 테이블에 account를 board_writer에 join할 것임
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account",nullable = false)
    private Member member;

    @Column(name = "baord_like")
    @Builder.Default
    private Long boardLike = 0L;

    @Column(name = "board_dislike",nullable = false)
    @Builder.Default
    private Long boardDislike = 0L;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime boardUpdateDateTime;

    @Column(name = "board_view_count")
    @Builder.Default
    private Long boardViewCount = 0L;

    @Column(name = "board_download_count")
    private Long boardDownloadCount;

    @Column(name = "score_img_url")
    private String scoreImgUrl;


}
