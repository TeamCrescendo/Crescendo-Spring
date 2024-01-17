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
@Table(name = "playlist")
public class PlayList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pl_no")
    private Long plNo;

    @ManyToOne
    @JoinColumn(name = "account", nullable = false)
    private AllPlayList account;

    @Column(name = "pl_adder")
    private String plAdder;

    @OneToOne
    @JoinColumn(name = "score_no", nullable = false)
    private Score score;

    @CreationTimestamp
    @Column(name = "pl_add_date_time")
    private LocalDateTime plAddDateTime;


}
