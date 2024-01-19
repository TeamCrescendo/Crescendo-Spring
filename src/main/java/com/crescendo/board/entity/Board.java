package com.crescendo.board.entity;

import com.crescendo.entity.Member;
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

    @Column(name = "board_like")
    @Builder.Default
    private Long boardLike = 0L;

    @Column(name = "board_dislike")
    private DisLike boardDisLike = DisLike.NONE;

    public void dislike() {
        switch (boardDisLike) {
            case NONE:
            //dislike를 누르지 않은 상태에서 dislike를 누르면 dislike가 +1
                boardDisLike = DisLike.UPVOTE;
                break;
            case UPVOTE:
            //dislike를 누른 상태에서 dislike를 누르면 dislike가 -1
                boardDisLike = DisLike.DOWNVOTE;
                break;
            default:
                break;
        }
    }

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

    public enum DisLike {
        UPVOTE(1), DOWNVOTE(-1), NONE(0);

        private final int value;

        DisLike(int value) {
            this.value = value;
        }


    }
}
