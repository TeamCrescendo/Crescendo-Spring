package com.crescendo.likeAndDislike.entity;

import com.crescendo.board.entity.Board;
import com.crescendo.board.entity.Dislike;
import com.crescendo.board.entity.Like;
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
@Table(name = "like_And_dislike")
public class LikeAndDislike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_And_dislike_id")
    private Long likeAndDislikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_no", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account", nullable = false)
    private Member member;


    @Column(name = "board_like")
    @Builder.Default
    private boolean boardLike = false;


}
