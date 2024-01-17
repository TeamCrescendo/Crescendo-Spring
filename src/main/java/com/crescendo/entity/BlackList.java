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

    @Column(name = "board_dislike",nullable = false)
    @Builder.Default
    private Long boardDislike = 0L;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime blackDateTime;

    //join 더 살펴보기
}
