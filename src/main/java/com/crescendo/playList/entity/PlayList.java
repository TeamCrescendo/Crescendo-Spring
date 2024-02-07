package com.crescendo.playList.entity;

import com.crescendo.allPlayList.entity.AllPlayList;
import com.crescendo.board.entity.Board;
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
@Table(name = "playlist")
public class PlayList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pl_no")
    private Long plNo;

    @ManyToOne
    @JoinColumn(name = "pl_id", nullable = false)
    private AllPlayList plId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "score_no", nullable = false)
    private Score score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_no", nullable = true)
    private Board board;

    @CreationTimestamp
    @Column(name = "pl_add_date_time")
    private LocalDateTime plAddDateTime;

}
