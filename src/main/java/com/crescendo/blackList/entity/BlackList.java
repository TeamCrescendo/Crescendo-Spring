package com.crescendo.blackList.entity;

import com.crescendo.board.entity.Board;
import com.crescendo.member.entity.Member;
import lombok.*;
import net.bytebuddy.utility.nullability.MaybeNull;
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
@Table(name = "blacklist")
public class BlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long blackId;

    // board 테이블에 boardNo를 blackList테이블의 boardNo에 join할 것임
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_no",nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="account", nullable = false)
    private Member member;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime blackDateTime;

}
